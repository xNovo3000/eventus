package io.github.xnovo3000.eventus.controller;

import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class LoginControllerTest {

    private final MockMvc mockMvc;

    @Test
    public void login_Success() throws Exception {
        // Generate form fields
        Part username = new MockPart("username", "admin".getBytes());
        Part password = new MockPart("password", "admin".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/login")
                        .part(username, password)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    public void login_InvalidUsername() throws Exception {
        // Generate form fields
        Part username = new MockPart("username", "admin6".getBytes());
        Part password = new MockPart("password", "admin".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/login")
                        .part(username, password)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    public void login_InvalidPassword() throws Exception {
        // Generate form fields
        Part username = new MockPart("username", "admin".getBytes());
        Part password = new MockPart("password", "admin1".getBytes());
        // Make request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/login")
                        .part(username, password)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error"))
                .andDo(MockMvcResultHandlers.log());
    }

}