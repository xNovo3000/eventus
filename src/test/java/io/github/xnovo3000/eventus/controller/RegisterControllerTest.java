package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
import io.github.xnovo3000.eventus.util.EventusWebTest;
import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.val;
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
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class RegisterControllerTest {

    private final MockMvc mockMvc;

    private final UserRepository userRepository;

    @Test
    @Order(1)
    public void registerNewUser_Success() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user0@test.com".getBytes());
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
    public void registerNewUser_FailEmailAlreadyExist() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user0@test.com".getBytes());
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
    public void registerNewUser_FailUsernameAlreadyExist() throws Exception {
        // Generate form fields
        Part email = new MockPart("email", "user1@test.com".getBytes());
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
        Part email = new MockPart("email", "user0test.com".getBytes());
        Part username = new MockPart("username", "user0".getBytes());
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
        userRepository.findByUsername("user0").ifPresent(userRepository::delete);
    }

}