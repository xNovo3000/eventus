package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import io.github.xnovo3000.eventus.dto.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.entity.Event;
import io.github.xnovo3000.eventus.entity.User;
import io.github.xnovo3000.eventus.repository.EventRepository;
import io.github.xnovo3000.eventus.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class EventServiceTest {

    private final EventService eventService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

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
                    event.setId((long) value);
                    event.setName("Event " + value);
                    event.setDescription("Description " + value);
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
    public void getOngoingEvents_ExpectOnlyOne() {
        Assertions.assertEquals(1, eventService.getOngoingEvents().size());
    }

    @Test
    public void getFutureEvents_ExpectNonApprovedAreNotPresent() {
        List<EventBriefDto> events = eventService.getFutureEvents(1).getContent();
        for (EventBriefDto event : events) {
            Assertions.assertNotEquals(0, event.getId() % 15);
        }
    }

    @Test
    public void proposeEvent_Success() {
        // Create DTO
        ProposeEventDtoZoned eventDtoZoned = new ProposeEventDtoZoned();
        eventDtoZoned.setName("name");
        eventDtoZoned.setDescription("des");
        eventDtoZoned.setStart(OffsetDateTime.now().plusHours(1));
        eventDtoZoned.setEnd(OffsetDateTime.now().plusHours(3));
        eventDtoZoned.setSeats(2);
        // Test
        Assertions.assertNotEquals(Optional.empty(), eventService.proposeEvent(eventDtoZoned, "admin"));
    }

    @Test
    public void proposeEvent_ErrorStartAfterEnd() {
        // Create DTO
        ProposeEventDtoZoned eventDtoZoned = new ProposeEventDtoZoned();
        eventDtoZoned.setName("name");
        eventDtoZoned.setDescription("des");
        eventDtoZoned.setStart(OffsetDateTime.now().plusHours(3));
        eventDtoZoned.setEnd(OffsetDateTime.now().plusHours(1));
        eventDtoZoned.setSeats(2);
        // Test
        Assertions.assertEquals(Optional.empty(), eventService.proposeEvent(eventDtoZoned, "admin"));
    }

}