package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.annotation.EventusTest;
import io.github.xnovo3000.eventus.api.dto.input.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.api.entity.Event;
import io.github.xnovo3000.eventus.api.entity.User;
import io.github.xnovo3000.eventus.api.repository.EventRepository;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import io.github.xnovo3000.eventus.api.service.EventService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
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
        user.setUsername("user101");
        user.setEmail("user101@test.com");
        user.setPassword(passwordEncoder.encode("user101"));
        user.setActive(true);
        userRepository.save(user);
        // Create env
        val now = OffsetDateTime.now();
        val adminUser = userRepository.findByUsername("admin").orElseThrow();
        // Create normal events
        val events = IntStream.range(0, 50)
                .mapToObj(id -> {
                    val event = new Event();
                    event.setId(id + 1L);
                    event.setName("Event " + id);
                    event.setDescription("Description " + id);
                    event.setCreator(adminUser);
                    event.setStart(now.minusDays(1).plusHours(id));
                    event.setEnd(now.minusDays(1).plusHours(id + 1));
                    event.setSeats(8);
                    event.setApproved(id < 30 && id > 2);
                    return event;
                })
                .toList();
        eventRepository.saveAll(events);
    }

    @Test
    @Order(1)
    public void getOngoingEvents_ExactSize() {
        Assertions.assertEquals(1, eventService.getOngoingEvents().size());
    }

    @Test
    @Order(1)
    public void getFutureEvents_ExactSize() {
        Assertions.assertEquals(5, eventService.getFutureEvents(1).getContent().size());
    }

    @Test
    @Order(1)
    public void getProposed_ExactPageSize() {
        Assertions.assertEquals(2, eventService.getProposed(1).getTotalPages());
    }

    @Test
    @Order(2)
    @WithUserDetails("admin")
    public void proposeEvent_StartAfterEnd() {
        val tomorrow = OffsetDateTime.now().plusDays(1);
        val proposal = new ProposeEventDtoZoned();
        proposal.setName("Proposal 1");
        proposal.setDescription("Desc 1");
        proposal.setSeats(8);
        proposal.setStart(tomorrow);
        proposal.setEnd(tomorrow.minusHours(1));
        Assertions.assertEquals(Optional.empty(), eventService.proposeEvent(proposal));
    }

    @Test
    @Order(2)
    @WithUserDetails("admin")
    public void proposeEvent_Success() {
        val tomorrow = OffsetDateTime.now().plusDays(1);
        val proposal = new ProposeEventDtoZoned();
        proposal.setName("Proposal 1");
        proposal.setDescription("Desc 1");
        proposal.setSeats(8);
        proposal.setStart(tomorrow);
        proposal.setEnd(tomorrow.plusHours(1));
        Assertions.assertNotEquals(Optional.empty(), eventService.proposeEvent(proposal));
    }

    @Test
    @Order(3)
    public void approveEvent_AlreadyApproved() {
        // Get the first approved event and try to re-approve it
        val approvedEvent = eventService.getFutureEvents(1).getContent().get(0);
        Assertions.assertFalse(eventService.approveEvent(approvedEvent.getId()));
    }

    @Test
    @Order(3)
    public void approveEvent_StartAfterNow() {
        // Get the first proposed event with start before now and try to approve him
        val pastEvent = eventService.getById(1L).orElseThrow();
        Assertions.assertFalse(eventService.approveEvent(pastEvent.getId()));
    }

    @Test
    @Order(3)
    public void approveEvent_Success() {
        // Get the first proposed event with start after now and try to approve him
        val proposedEvent = eventService.getProposed(1).getContent().get(0);
        Assertions.assertTrue(eventService.approveEvent(proposedEvent.getId()));
    }

    @Test
    @Order(4)
    public void rejectEvent_AlreadyApproved() {
        // Get the first future event and try to reject him
        val approvedEvent = eventService.getFutureEvents(1).getContent().get(0);
        Assertions.assertFalse(eventService.rejectEvent(approvedEvent.getId()));
    }

    @Test
    @Order(4)
    public void rejectEvent_Success() {
        // Get the first proposed event and try to reject him
        val proposedEvent = eventService.getProposed(1).getContent().get(0);
        Assertions.assertTrue(eventService.rejectEvent(proposedEvent.getId()));
    }

    @Test
    @Order(5)
    public void subscribeUserToEvent_InvalidUser() {

    }

    @Test
    @Order(5)
    public void subscribeUserToEvent_InvalidEvent() {

    }

    @AfterAll
    @Transactional
    public void destroy() {
        // Delete user
        userRepository.findByUsername("user101").ifPresent(userRepository::delete);
        // Delete events
        eventRepository.deleteAll();
    }

}