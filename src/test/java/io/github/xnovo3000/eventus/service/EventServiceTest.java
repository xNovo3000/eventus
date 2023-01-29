package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.entity.Event;
import io.github.xnovo3000.eventus.entity.User;
import io.github.xnovo3000.eventus.repository.EventRepository;
import io.github.xnovo3000.eventus.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventServiceTest {

    private final EventService eventService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventServiceTest(
            @Autowired EventService eventService,
            @Autowired EventRepository eventRepository,
            @Autowired UserRepository userRepository
    ) {
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    @Transactional
    public void createEnvironment() {
        // Get admin user (must exist)
        User user = userRepository.findByUsername("admin").orElseThrow();
        // Create the events
        List<Event> events = IntStream.range(0, 100)
                .mapToObj(value -> {
                    OffsetDateTime start = OffsetDateTime.now().minusDays(1).minusMinutes(1).plusHours(value);
                    Event event = new Event();
                    event.setName("Event " + value);
                    event.setDescription("Description" + value);
                    event.setCreator(user);
                    event.setStart(start);
                    event.setEnd(start.plusHours(1));
                    event.setSeats(8);
                    event.setApproved(value % 15 != 0);
                    return event;
                })
                .toList();
        // Save into database
        eventRepository.saveAll(events);
    }

    @Test
    public void test1() {
        eventService.getOngoingEvents();
        eventService.getFutureEvents(1);
    }

}