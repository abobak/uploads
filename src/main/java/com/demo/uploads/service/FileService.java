package com.demo.uploads.service;

import com.demo.uploads.configuration.FileStorageConfiguration;
import com.demo.uploads.exception.BadRequestException;
import com.demo.uploads.exception.FileStorageException;
import com.demo.uploads.exception.NotFoundException;
import com.demo.uploads.model.SharedFile;
import com.demo.uploads.model.User;
import com.demo.uploads.repository.SharedFileRepository;
import com.demo.uploads.repository.UserRepository;
import lombok.Getter;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Random;

@Service
public class FileService {

    @Getter
    private final Path fileStorageLocation;

    private final SharedFileRepository sharedFileRepository;

    private final UserRepository userRepository;

    private final Object newShareableFileLock = new Object();

    @Autowired
    public FileService(FileStorageConfiguration fileStorageConfiguration,
                       SharedFileRepository sharedFileRepository,
                       UserRepository userRepository) throws FileStorageException {

        this.fileStorageLocation = Paths.get(fileStorageConfiguration.getUploadDir())
                .toAbsolutePath().normalize();
        this.sharedFileRepository = sharedFileRepository;
        this.userRepository = userRepository;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    private String storeFile(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {

            if (fileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName, ex);
        }
    }

    @Transactional
    @Synchronized("newShareableFileLock")
    public String createShareableFile(MultipartFile f, User u) {
        String path = storeFile(f);
        String identifier = "";
        while (true) {
            identifier = createIdentifier();
            Optional<SharedFile> shouldNotExist = sharedFileRepository.findByIdentifier(identifier);
            if (!shouldNotExist.isPresent()) {
                break;
            }
        }
        SharedFile file = new SharedFile();
        file.setLocalPath(path);
        file.setName(f.getOriginalFilename());
        file.setIdentifier(identifier);
        file.setOwner(u);
        u.getMyFiles().add(file);
        sharedFileRepository.save(file);
        userRepository.save(u);
        return file.getIdentifier();
    }

    private String createIdentifier() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Transactional
    public void shareWithOtherUser(String shareIdentifier, String sharedWithEmail, User currentUser) {
        SharedFile sf = sharedFileRepository.findByIdentifier(shareIdentifier).orElseThrow(() -> new NotFoundException("No file with identifier " + shareIdentifier));
        if (!currentUser.equals(sf.getOwner())) {
            throw new BadRequestException("You can't share file you don't own!");
        }
        User userWhoMightAccessFile = userRepository.findUserByEmail(sharedWithEmail);
        userWhoMightAccessFile.getSharedWithMe().add(sf);
        sf.getSharedWith().add(userWhoMightAccessFile);
        userRepository.save(userWhoMightAccessFile);
        sharedFileRepository.save(sf);
    }
}
