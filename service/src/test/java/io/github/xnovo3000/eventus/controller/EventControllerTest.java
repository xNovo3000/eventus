package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.annotation.EventusWebTest;
import io.github.xnovo3000.eventus.api.entity.Event;
import io.github.xnovo3000.eventus.api.repository.EventRepository;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@EventusWebTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EventControllerTest {

    private final MockMvc mockMvc;

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @BeforeAll
    @Transactional
    public void setup() {
        // Create subscribable event
        val event = new Event();
        event.setName("Event 1");
        event.setDescription("Description 1");
        event.setCreator(userRepository.findByUsername("user0").orElseThrow());
        event.setCreationDate(OffsetDateTime.now().minusHours(1));
        event.setStart(OffsetDateTime.now().plusDays(1));
        event.setEnd(OffsetDateTime.now().plusDays(1).plusHours(1));

        // Create rateable event
    }

    public void get_NotLogged() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/event/1"));
    }

}