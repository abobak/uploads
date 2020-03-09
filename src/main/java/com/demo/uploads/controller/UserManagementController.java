package com.demo.uploads.controller;

import com.demo.uploads.api.UserManagementApi;
import com.demo.uploads.dto.NewUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserManagementController implements UserManagementApi {

    public void registerUser(@RequestBody NewUserDto dto) {

    }
}
