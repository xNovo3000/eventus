package io.github.xnovo3000.eventus.controller;

import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class ActionControllerTest {

    private final MockMvc mockMvc;

    @Test
    @WithUserDetails("admin")
    public void proposeNewEvent_Success() throws Exception {
        // Get time
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusDays(1);
        // Create parts
        Part name = new MockPart("name", "Event 1".getBytes());
        Part description = new MockPart("description", "Description 1".getBytes());
        Part start = new MockPart("start", startTime.toString().getBytes());
        Part end = new MockPart("end", endTime.toString().getBytes());
        Part seats = new MockPart("seats", "4".getBytes());
        // Send request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/action/propose_event")
                        .part(name, description, start, end, seats)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/event/*"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithAnonymousUser
    public void proposeNewEvent_NotLogged() throws Exception {
        // Get time
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusDays(1);
        // Create parts
        Part name = new MockPart("name", "Event 1".getBytes());
        Part description = new MockPart("description", "Description 1".getBytes());
        Part start = new MockPart("start", startTime.toString().getBytes());
        Part end = new MockPart("end", endTime.toString().getBytes());
        Part seats = new MockPart("seats", "4".getBytes());
        // Send request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/action/propose_event")
                        .part(name, description, start, end, seats)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"))
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("admin")
    public void proposeNewEvent_InvalidData() throws Exception {
        // Get time
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusDays(1);
        // Create parts
        Part name = new MockPart("name", "Event 1".getBytes());
        Part description = new MockPart("description", "Description 1".getBytes());
        Part start = new MockPart("start", startTime.toString().getBytes());
        Part end = new MockPart("end", endTime.toString().getBytes());
        Part seats = new MockPart("seats", "0".getBytes());
        // Send request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/action/propose_event")
                        .part(name, description, start, end, seats)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.log());
    }

}