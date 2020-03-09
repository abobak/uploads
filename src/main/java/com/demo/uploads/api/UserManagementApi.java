package com.demo.uploads.api;

import com.demo.uploads.dto.NewUserDto;

public interface UserManagementApi {

    void registerUser(NewUserDto dto);
}
