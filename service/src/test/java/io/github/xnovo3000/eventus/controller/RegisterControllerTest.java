package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.api.repository.UserRepository;
import io.github.xnovo3000.eventus.annotation.EventusWebTest;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@EventusWebTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RegisterControllerTest {

    private final MockMvc mockMvc;

    private final UserRepository userRepository;

    @Test
    @Order(1)
    public void registerNewUser_Success() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user101@eventus.com".getBytes());
        Part username = new MockPart("username", "user101".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
                        .part(email, username)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @Order(2)
    public void registerNewUser_FailEmailAlreadyExist() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user101@eventus.com".getBytes());
        Part username = new MockPart("username", "user1".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
                        .part(email, username)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @Order(3)
    public void registerNewUser_FailUsernameAlreadyExist() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user1@eventus.com".getBytes());
        Part username = new MockPart("username", "user101".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
                        .part(email, username)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @Order(4)
    public void registerNewUser_InvalidEmail() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user101test.com".getBytes());
        Part username = new MockPart("username", "user101".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/register")
                        .part(email, username)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.log());
    }

    @AfterAll
    @Transactional
    public void destroy() {
        // Find created user in step 1 and destroy him
        userRepository.findByUsername("user101").ifPresent(userRepository::delete);
    }

}