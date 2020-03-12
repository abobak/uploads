package com.demo.uploads.controller;

import com.demo.uploads.repository.UserRepository;
import com.demo.uploads.security.RestApiSecurityConfig;
import com.demo.uploads.security.UploadAppAuthenticationProvider;
import com.demo.uploads.security.UserSecurityHelper;
import com.demo.uploads.service.FilesService;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {SharingController.class, RestApiSecurityConfig.class})
@WebMvcTest
class SharingControllerTest {

    @MockBean
    private UploadAppAuthenticationProvider uploadAppAuthenticationProvider;

    @MockBean
    private UserSecurityHelper userSecurityHelper;

    @MockBean
    private FilesService filesService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UsersService usersService;

    @Autowired
    private MockMvc mvc;

    @Test
    void requestingForSharesShouldThrow401ForUnauthorizedUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/file")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()
                );
    }

    @Test
    void requestingForSharesShouldReturn200ForAuthorizedUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/file")
                .with(user("user").password("pass"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()
                );
    }

    @ParameterizedTest
    @MethodSource("malformedPayloads")
    void shouldThrowBadRequestForMalformedPayloads(String jsonPayload) throws  Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/share")
                .with(user("user").password("pass"))
                .content(jsonPayload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()
                );
    }

    @Test
    void shouldReturn200ForCorrectPayload() throws  Exception {
        String jsonPayload = "{\"shared_with\": \"someone@email.com\", \"share_identifier\": \"abcdefghij\" }";
        mvc.perform(MockMvcRequestBuilders
                .post("/api/share")
                .with(user("user").password("pass"))
                .content(jsonPayload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()
                );
    }

    private static Stream<Arguments> malformedPayloads() {
        return Stream.of(
                Arguments.of("{ \"shared_with\": \"user@validdomain.com\""),
                Arguments.of("{ }"),
                Arguments.of("{ \"share_identifier\": \"abcdefghij\" }")
        );
    }
}
