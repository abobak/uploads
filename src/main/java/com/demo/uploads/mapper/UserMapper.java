package com.demo.uploads.mapper;

import com.demo.uploads.dto.NewUserDto;
import com.demo.uploads.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User dtoToNewUser(NewUserDto dto);
}
