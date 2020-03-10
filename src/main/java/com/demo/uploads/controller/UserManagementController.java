package com.demo.uploads.controller;

import com.demo.uploads.api.UserManagementApi;
import com.demo.uploads.dto.NewUserDto;
import com.demo.uploads.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserManagementController implements UserManagementApi {

    private final UsersService usersService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/register")
    public void registerUser(@Valid @RequestBody NewUserDto dto) {
        usersService.createUser(dto);
    }
}
