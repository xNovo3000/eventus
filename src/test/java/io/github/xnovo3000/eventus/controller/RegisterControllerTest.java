package io.github.xnovo3000.eventus.controller;

import jakarta.servlet.http.Part;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegisterControllerTest {

    private final MockMvc mockMvc;

    public RegisterControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    public void registerNewUser_Success() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user0@email.com".getBytes());
        Part username = new MockPart("username", "user0".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
                        .part(email, username)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?register_success"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @Order(2)
    public void registerNewUser_FailUsernameAlreadyExist() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user0@email.com".getBytes());
        Part username = new MockPart("username", "user1".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
                        .part(email, username)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register?error"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @Order(3)
    public void registerNewUser_FailEmailAlreadyExist() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user1@email.com".getBytes());
        Part username = new MockPart("username", "user0".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
                        .part(email, username)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register?error"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @Order(4)
    public void registerNewUser_InvalidEmail() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user0email.com".getBytes());
        Part username = new MockPart("username", "user0".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
                        .part(email, username)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register?invalid_email"))
                .andDo(MockMvcResultHandlers.log());
    }

}