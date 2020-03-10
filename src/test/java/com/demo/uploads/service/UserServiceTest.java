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
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldPersistUserFromDto() {
        // given
        String expectedEmail = "user@domain.com";
        String expectedPassword = "password";
        NewUserDto dto = new NewUserDto(expectedEmail, expectedPassword);

        // when
        userService.createUser(dto);

        // then
        User u = userService.loadUserByUsername(expectedEmail);
        assertEquals(expectedEmail, u.getUsername());
        assertEquals(expectedPassword, u.getPassword());
    }
}
