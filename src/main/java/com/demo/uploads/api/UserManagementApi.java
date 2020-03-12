package com.demo.uploads.api;

import com.demo.uploads.dto.NewUserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "users", tags = { "users" })
public interface UserManagementApi {

    @ApiOperation(value = "Create new user with given email and password")
    void registerUser(NewUserDto dto);
}
