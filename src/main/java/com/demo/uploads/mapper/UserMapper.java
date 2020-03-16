package com.demo.uploads.mapper;

import com.demo.uploads.dto.NewUserDto;
import com.demo.uploads.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Mapper(componentModel = "spring", imports = BCrypt.class)
public interface UserMapper {

    @Mapping(target = "password", expression = "java(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()))")
    User dtoToNewUser(NewUserDto dto);
}
