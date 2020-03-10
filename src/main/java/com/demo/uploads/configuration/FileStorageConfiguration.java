package com.demo.uploads.configuration;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class FileStorageConfiguration {

    @Value("${com.demo.uploads.directory}")
    private String uploadDir;
}
