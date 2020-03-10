package com.demo.uploads.controller;

import com.demo.uploads.api.FilesApi;
import com.demo.uploads.model.User;
import com.demo.uploads.security.UserSecurityHelper;
import com.demo.uploads.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FilesController implements FilesApi {

    private final FileService fileService;

    private final UserSecurityHelper userSecurityHelper;

    @PostMapping(path = "/api/file")
    public String uploadFile(@RequestParam("file") MultipartFile f) {
        User user = userSecurityHelper.getCurrentUser();
        return fileService.createShareableFile(f, user);
    }
}
