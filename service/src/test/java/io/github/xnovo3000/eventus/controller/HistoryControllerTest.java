package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.annotation.EventusWebTest;
import io.github.xnovo3000.eventus.api.entity.Authority;
import io.github.xnovo3000.eventus.api.repository.AuthorityRepository;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class HistoryControllerTest {

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
        mockMvc.perform(MockMvcRequestBuilders.get("/history")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("user0")
    public void get_NotEventManager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/history")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.log());
    }

    @Test
    @WithUserDetails("user1")
    public void get_Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/history")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.log());
    }

}