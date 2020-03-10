package com.demo.uploads.controller;

import com.demo.uploads.repository.UserRepository;
import com.demo.uploads.security.RestApiSecurityConfig;
import com.demo.uploads.security.UploadAppAuthenticationProvider;
import com.demo.uploads.service.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserManagementController.class, RestApiSecurityConfig.class, UploadAppAuthenticationProvider.class})
@WebMvcTest
class UserManagementControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsersService usersService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturn201ForValidInput() throws Exception {
        String jsonString = "{ \"email\": \"user@validdomain.com\", \"password\": \"my_secret_password\"}";
        mvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(jsonString).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("malformedInputs")
    void shouldThrow400ForMalformedInput(String jsonString) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(jsonString).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> malformedInputs() {
        return Stream.of(
                Arguments.of("{ \"mail\": \"user@validdomain.com\", \"pass\": \"my_secret_password\"}"),
                Arguments.of("{ \"email\": \"user@validdomain.com\"}")
        );
    }
}
