package com.demo.uploads.api;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FilesApi {

    String uploadFile(MultipartFile f);

    ResponseEntity<Resource> getContent(String identifier);
}
