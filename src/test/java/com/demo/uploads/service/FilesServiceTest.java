package com.demo.uploads.service;

import com.demo.uploads.dto.FileSharesDto;
import com.demo.uploads.exception.AccessDeniedException;
import com.demo.uploads.exception.BadRequestException;
import com.demo.uploads.exception.NotFoundException;
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
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilesServiceTest {

    private MultipartFile d1 = new MockMultipartFile("file1ThatDoesNotExists.txt",
            "file1ThatDoesNotExists.txt",
            "text/plain",
            "This is a dummy file content".getBytes(StandardCharsets.UTF_8));

    private MultipartFile d2 = new MockMultipartFile("file2ThatDoesNotExists.txt",
            "file2ThatDoesNotExists.txt",
            "text/plain",
            "This is a dummy file content".getBytes(StandardCharsets.UTF_8));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SharedFileRepository sharedFileRepository;

    @Autowired
    private FilesService filesService;

    private Long userId;

    private User u;

    @BeforeEach
    void setUp() {
        u = new User();
        u.setEmail("me@domain.com");
        u.setPassword("plaintext");
        u.setMyFiles(new HashSet<>());
        u.setSharedWithMe(new HashSet<>());
        userId = userRepository.save(u).getId();
    }

    @AfterAll
    void tearDown() throws IOException {
        Path target = filesService.getFileStorageLocation();
        FileUtils.deleteDirectory(target.toFile());
    }

    @Test
    void shouldAddFileToUser_whenUserUploadsFile() {
        // when
        filesService.createShareableFile(d1, u);
        // then
        u = userRepository.findWithMyFiles(userId);
        assertEquals(1, u.getMyFiles().size());
    }

    @Test
    void shouldPersistFileToDisk_whenUserUploadsFile() {
        // given
        String identifier = filesService.createShareableFile(d1, u);
        // when
        SharedFile sf = sharedFileRepository.findByIdentifier(identifier).orElseThrow(() -> new RuntimeException("Test in error"));
        String location = sf.getLocalPath();
        File f = new File(location);
        // then
        assertTrue(f.exists());
    }

    @Test
    void shouldAddFileToFilesSharedWithUser() {
        // given
        User u1 = createUser();
        String identifier = filesService.createShareableFile(d1, u);
        // when
        filesService.shareWithOtherUser(identifier, u1.getEmail(), u);
        u1 = userRepository.findWithFilesSharedWithMe(u1.getId());
        SharedFile sf = sharedFileRepository.findByIdentifier(identifier).orElseThrow(() -> new RuntimeException("Test data corrupted"));
        // then
        assertTrue(u1.getSharedWithMe().contains(sf));
        assertTrue(sf.getSharedWith().contains(u1));
    }

    @Test
    void shouldReturnTwoSharedFilesAndZeroOwned() {
        // given
        User u1 = createUser();
        String identifier = filesService.createShareableFile(d1, u);
        String identifier2 = filesService.createShareableFile(d2, u);
        // when
        filesService.shareWithOtherUser(identifier, u1.getEmail(), u);
        filesService.shareWithOtherUser(identifier2, u1.getEmail(), u);
        // then
        FileSharesDto available = filesService.getAvailableFiles(u1);
        assertEquals(0, available.getMyFiles().size());
        assertEquals(2, available.getSharedWithMe().size());
    }

    @Test
    void shouldThrowErrorIfNonOwnerTriesToShareFile() {
        // given
        User u1 = createUser();
        String identifier = filesService.createShareableFile(d1, u);
        // when & then
        User notAllowedToShare = u1;
        assertThrows(BadRequestException.class, () -> filesService.shareWithOtherUser(identifier, u.getEmail(), notAllowedToShare));
    }

    @Test
    void shouldExposeResourceWhenValidUserWantsToAccessFile() {
        // given
        String id = filesService.createShareableFile(d1, u);
        // when
        Resource r = filesService.getFileContent(id, u);
        // then
        assertTrue(r.exists());
    }

    @Test
    void shouldBeAbleToUploadTheSameFileTwiceAndStoreBothCopies() {
        // given
        String id1 = filesService.createShareableFile(d1, u);
        String id2 = filesService.createShareableFile(d1, u);
        // when
        Resource r1 = filesService.getFileContent(id1, u);
        Resource r2 = filesService.getFileContent(id2, u);
        // then
        assertTrue(r1.exists());
        assertTrue(r2.exists());
    }

    @Test
    void shouldBeAbleToShareTheSameFileTwice() {
        // given
        String id1 = filesService.createShareableFile(d1, u);
        String id2 = filesService.createShareableFile(d1, u);
        User u1 = createUser();

        // when
        filesService.shareWithOtherUser(id1, u1.getEmail(), u);
        filesService.shareWithOtherUser(id2, u1.getEmail(), u);

        // then
        FileSharesDto available = filesService.getAvailableFiles(u1);
        assertEquals(2, available.getSharedWithMe().size());
    }

    @Test
    void whenUserSharesFileWithTheSameIdentifierTwiceItShouldBeAddedToSharedFilesOnlyOnce() {
        // given
        String id1 = filesService.createShareableFile(d1, u);
        User u1 = createUser();

        // when
        filesService.shareWithOtherUser(id1, u1.getEmail(), u);
        filesService.shareWithOtherUser(id1, u1.getEmail(), u);

        // then
        u1 = userRepository.findWithFilesSharedWithMe(u1.getId());
        assertEquals(1, u1.getSharedWithMe().size());
    }

    @Test
    void shouldThrowExceptionWhenInvalidUserWantsToAccessFile() {
        // given
        String id = filesService.createShareableFile(d1, u);
        User userWithNoAccessToFile = createUser();
        // when & then
        assertThrows(AccessDeniedException.class, () -> filesService.getFileContent(id, userWithNoAccessToFile));
    }

    @Test
    void shouldThrowExceptionIfUserTriesToDownloadFileWithIdentifierThatDoesntExist() {
        // when & then
        assertThrows(NotFoundException.class, () -> filesService.getFileContent("i_dont_exist_1111!", u));
    }

    private User createUser() {
        User u1 = new User();
        u1.setEmail("share_with_me@domain.com");
        u1.setPassword("plaintext");
        u1.setMyFiles(new HashSet<>());
        u1.setSharedWithMe(new HashSet<>());
        u1 = userRepository.save(u1);
        return u1;
    }


}
