package com.demo.uploads.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileStorageException extends RuntimeException {

    public FileStorageException(String s, Exception e) {
        super(s);
        log.error(s, e);
    }

    public FileStorageException(String s) {
        throw new BadRequestException(s);
    }
}
