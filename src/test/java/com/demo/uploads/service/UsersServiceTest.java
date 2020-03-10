package com.demo.uploads.service;

import com.demo.uploads.dto.NewUserDto;
import com.demo.uploads.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UsersServiceTest {

    @Autowired
    private UsersService usersService;

    @Test
    void shouldPersistUserFromDto() {
        // given
        String expectedEmail = "user@domain.com";
        String expectedPassword = "password";
        NewUserDto dto = new NewUserDto(expectedEmail, expectedPassword);

        // when
        usersService.createUser(dto);

        // then
        User u = usersService.loadUserByUsername(expectedEmail);
        assertEquals(expectedEmail, u.getUsername());
        assertEquals(expectedPassword, u.getPassword());
    }
}
