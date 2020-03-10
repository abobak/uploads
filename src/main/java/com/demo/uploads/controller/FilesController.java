package com.demo.uploads.controller;

import com.demo.uploads.api.FilesApi;
import com.demo.uploads.model.User;
import com.demo.uploads.security.UserSecurityHelper;
import com.demo.uploads.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FilesController implements FilesApi {

    private final FilesService filesService;

    private final UserSecurityHelper userSecurityHelper;

    @PostMapping(path = "/api/file")
    public String uploadFile(@RequestParam("file") MultipartFile f) {
        User user = userSecurityHelper.getCurrentUser();
        return filesService.createShareableFile(f, user);
    }

    @GetMapping(path = "/api/file/{identifier}")
    public ResponseEntity<Resource> getContent(@PathVariable String identifier) {
        User user = userSecurityHelper.getCurrentUser();
        Resource r = filesService.getFileContent(identifier, user);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + r.getFilename() + "\"")
                .body(r);
    }
}
