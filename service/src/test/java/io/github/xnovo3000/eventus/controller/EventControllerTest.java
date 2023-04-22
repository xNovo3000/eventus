package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.annotation.EventusWebTest;
import io.github.xnovo3000.eventus.api.entity.Authority;
import io.github.xnovo3000.eventus.api.repository.AuthorityRepository;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@EventusWebTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EventControllerTest {

    private final MockMvc mockMvc;

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @BeforeAll
    @Transactional
    public void setup() {
        // Set user1 as EVENT_MANAGER
        val newAuthority = new Authority();
        newAuthority.setUser(userRepository.findByUsername("user1").orElseThrow());
        newAuthority.setName("EVENT_MANAGER");
        try {
            authorityRepository.save(newAuthority);
        } catch (Exception ignored) {}
    }

    @Test
    @WithAnonymousUser
    public void get_NotLogged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/event/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @Order(2)
    @WithUserDetails("user0")
    public void get_Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/event/1")
                        .header("Referer", "/event/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithAnonymousUser
    public void subscribe_NotLogged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/subscribe")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("user0")
    public void subscribe_Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/subscribe")
                        .header("Referer", "/event/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithAnonymousUser
    public void unsubscribe_NotLogged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/unsubscribe")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("user0")
    public void unsubscribe_Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/unsubscribe")
                        .header("Referer", "/event/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithAnonymousUser
    public void rate_NotLogged() throws Exception {
        // Create data
        val starsPart = new MockPart("stars", "5".getBytes());
        val commentPart = new MockPart("comment", "Comment here".getBytes());
        // Perform action
        mockMvc.perform(MockMvcRequestBuilders.multipart("/event/1/rate")
                        .part(starsPart, commentPart)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("user0")
    public void rate_Ok() throws Exception {
        // Create data
        val starsPart = new MockPart("stars", "5".getBytes());
        val commentPart = new MockPart("comment", "Comment here".getBytes());
        // Perform action
        mockMvc.perform(MockMvcRequestBuilders.multipart("/event/1/rate")
                        .part(starsPart, commentPart)
                        .header("Referer", "/event/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithAnonymousUser
    public void approve_NotLogged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/approve")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("user0")
    public void approve_NotEventManager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/approve")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("user1")
    public void approve_Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/approve")
                        .header("Referer", "/event/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithAnonymousUser
    public void reject_NotLogged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/reject")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("user0")
    public void reject_NotEventManager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/reject")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("user1")
    public void reject_Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/event/1/reject")
                        .header("Referer", "/event/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithAnonymousUser
    public void propose_NotLogged() throws Exception {
        val name = new MockPart("name", "Event 1".getBytes());
        val description = new MockPart("description", "Description 1".getBytes());
        val start = new MockPart("start", "2030-01-01T20:00".getBytes());
        val end = new MockPart("end", "2030-01-01T21:00".getBytes());
        val seats = new MockPart("name", "8".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/event/propose")
                        .part(name, description, start, end, seats)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @Order(1)
    @WithUserDetails("user0")
    public void propose_Ok() throws Exception {
        val name = new MockPart("name", "Event 1".getBytes());
        val description = new MockPart("description", "Description 1".getBytes());
        val start = new MockPart("start", "2030-01-01T20:00".getBytes());
        val end = new MockPart("end", "2030-01-01T21:00".getBytes());
        val seats = new MockPart("seats", "8".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/event/propose")
                        .part(name, description, start, end, seats)
                        .header("Referer", "/event/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

}