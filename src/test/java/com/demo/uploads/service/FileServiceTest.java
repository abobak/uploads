package com.demo.uploads.service;

import com.demo.uploads.model.SharedFile;
import com.demo.uploads.model.User;
import com.demo.uploads.repository.SharedFileRepository;
import com.demo.uploads.repository.UserRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileServiceTest {

    private MultipartFile d1 = new MockMultipartFile("file1ThatDoesNotExists.txt",
            "file1ThatDoesNotExists.txt",
            "text/plain",
            "This is a dummy file content".getBytes(StandardCharsets.UTF_8));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SharedFileRepository sharedFileRepository;

    @Autowired
    private FileService fileService;

    private Long userId;

    private User u;

    @BeforeEach
    void setUp() {
        u = new User();
        u.setEmail("me@domain.com");
        u.setPassword("plaintext");
        u.setMyFiles(new ArrayList<>());
        u.setSharedWithMe(new ArrayList<>());
        userId = userRepository.save(u).getId();
    }

    @AfterAll
    void tearDown() throws IOException {
        Path target = fileService.getFileStorageLocation();
        FileUtils.deleteDirectory(target.toFile());
    }

    @Test
    void shouldAddFileToUser_whenUserUploadsFile() {
        // when
        fileService.createShareableFile(d1, u);
        // then
        u = userRepository.findWithMyFiles(userId);
        assertEquals(1, u.getMyFiles().size());
    }

    @Test
    void shouldPersistFileToDisk_whenUserUploadsFile() {
        // given
        String identifier = fileService.createShareableFile(d1, u);
        // when
        SharedFile sf = sharedFileRepository.findByIdentifier(identifier).orElseThrow(() -> new RuntimeException("Test in error"));
        String location = sf.getLocalPath();
        File f = new File(location);
        // then
        assertTrue(f.exists());

    }

}
