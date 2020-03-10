package com.demo.uploads.controller;

import com.demo.uploads.api.FilesApi;
import com.demo.uploads.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FilesController implements FilesApi {

    private final FileService fileService;

    public String uploadFile(MultipartFile f) {
        return null;
    }
}
