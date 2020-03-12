package com.demo.uploads.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "files", tags = { "files" })
public interface FilesApi {

    @ApiOperation(value = "Upload file and receive file share identifier", response = String.class)
    String uploadFile(@ApiParam(name = "file", value = "Uploaded file content") MultipartFile file);

    @ApiOperation(value = "Download file content with given identifier", response = Resource.class)
    ResponseEntity<Resource> getContent(@ApiParam(
            name = "identifier",
            value = "10 character identifier of file requested for download",
            example = "abcdefghij") String identifier);
}
