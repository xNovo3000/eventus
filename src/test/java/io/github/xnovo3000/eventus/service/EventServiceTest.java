package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.bean.entity.Event;
import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.mvc.repository.EventRepository;
import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
import io.github.xnovo3000.eventus.mvc.service.EventService;
import io.github.xnovo3000.eventus.util.EventusTest;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.stream.IntStream;

@EventusTest
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class EventServiceTest {

    private final EventService eventService;

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final PasswordEncoder passwordEncoder;

    @BeforeAll
    @Transactional
    public void setup() {
        // Create the user
        val user = new User();
        user.setUsername("user0");
        user.setEmail("user0@test.com");
        user.setPassword(passwordEncoder.encode("user0"));
        user.setActive(true);
        userRepository.save(user);
        // Create env
        val now = OffsetDateTime.now();
        val adminUser = userRepository.findByUsername("admin").orElseThrow();
        // Create normal events
        val events = IntStream.range(0, 50)
                .mapToObj(id -> {
                    val event = new Event();
                    event.setName("Event " + id);
                    event.setDescription("Description " + id);
                    event.setCreator(adminUser);
                    event.setStart(now.minusDays(1).plusHours(id));
                    event.setEnd(now.minusDays(1).plusHours(id + 1));
                    event.setSeats(8);
                    event.setApproved(id < 30);
                    return event;
                })
                .toList();
        eventRepository.saveAll(events);
    }

    @Test
    public void getOngoingEvents_ExactSize() {
        Assertions.assertEquals(1, eventService.getOngoingEvents().size());
    }

    @Test
    public void getFutureEvents_ExactSize() {
        Assertions.assertEquals(5, eventService.getFutureEvents(1).getContent().size());
    }

    @Test
    public void getProposed_ExactPageSize() {
        Assertions.assertEquals(2, eventService.getProposed(1).getTotalPages());
    }

    @AfterAll
    @Transactional
    public void destroy() {
        // Delete user
        userRepository.findByUsername("user0").ifPresent(userRepository::delete);
        // Delete events
        eventRepository.deleteAll();
    }

}