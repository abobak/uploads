package com.demo.uploads.api;

import org.springframework.web.multipart.MultipartFile;

public interface FilesApi {

    String uploadFile(MultipartFile f);
}
