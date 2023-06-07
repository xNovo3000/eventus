package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.annotation.EventusTest;
import io.github.xnovo3000.eventus.api.dto.input.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.api.dto.input.RateFormDto;
import io.github.xnovo3000.eventus.api.entity.Event;
import io.github.xnovo3000.eventus.api.entity.Subscription;
import io.github.xnovo3000.eventus.api.repository.EventRepository;
import io.github.xnovo3000.eventus.api.repository.SubscriptionRepository;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import io.github.xnovo3000.eventus.api.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

@EventusTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EventServiceTest {

    private final EventService eventService;

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final RateFormDto rateFormDto = new RateFormDto();

    @BeforeAll
    @Transactional
    public void setup() {
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
                    event.setSeats(1);
                    event.setApproved(id > 0 && id < 38);
                    return event;
                })
                .toList();
        eventRepository.saveAll(events);
        // Set user0 as participant for event ID 30 (not started already)
        val event30_user0_Subscription = new Subscription();
        event30_user0_Subscription.setCreationDate(OffsetDateTime.now());
        event30_user0_Subscription.setEvent(eventRepository.findById(30L).orElseThrow());
        event30_user0_Subscription.setUser(userRepository.findByUsername("user0").orElseThrow());
        subscriptionRepository.save(event30_user0_Subscription);
        // Set user1 as participant for event ID 12 (already finished, can rate the event)
        val event12_user1_Subscription = new Subscription();
        event12_user1_Subscription.setCreationDate(OffsetDateTime.now());
        event12_user1_Subscription.setEvent(eventRepository.findById(12L).orElseThrow());
        event12_user1_Subscription.setUser(userRepository.findByUsername("user1").orElseThrow());
        subscriptionRepository.save(event12_user1_Subscription);
        // Create rate form DTO
        rateFormDto.setStars(5);
        rateFormDto.setComment("Comment number 1");
    }

    @Test
    @Order(1)
    public void getOngoingEvents_ExactSize() {
        Assertions.assertEquals(1, eventService.getOngoingEvents().size());
    }

    @Test
    @Order(1)
    public void getFutureEvents_ExactPageSize() {
        Assertions.assertEquals(2, eventService.getFutureEvents(1).getTotalPages());
    }

    @Test
    @Order(1)
    public void getProposedEvents_ExactPageSize() {
        Assertions.assertEquals(12, eventService.getProposedEvents(1).getContent().size());
    }

    @Test
    @Order(1)
    public void getHistory_ExactPageSize() {
        Assertions.assertEquals(5, eventService.getHistory(1).getTotalPages());
    }

    @Test
    @Order(1)
    @WithUserDetails("user0")
    public void getEventsThatUserParticipated_ExactSize() {
        Assertions.assertEquals(1, eventService.getEventsThatUserParticipated(1).getContent().size());
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
    public void approveEvent_StartBeforeNow() {
        // Get an event with start before now and try to approve him
        Assertions.assertFalse(eventService.approveEvent(1));
    }

    @Test
    @Order(3)
    public void approveEvent_Success() {
        // Get the event with ID 48 and 49 and approve
        Assertions.assertTrue(eventService.approveEvent(48));
        Assertions.assertTrue(eventService.approveEvent(49));
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
        val proposedEvent = eventService.getProposedEvents(1).getContent().get(0);
        Assertions.assertTrue(eventService.rejectEvent(proposedEvent.getId()));
    }

    @Test
    @Order(10)
    public void subscribeUserToEvent_InvalidUser() {
        Assertions.assertFalse(eventService.subscribeUserToEvent(30, "user101"));
    }

    @Test
    @Order(10)
    public void subscribeUserToEvent_InvalidEvent() {
        Assertions.assertFalse(eventService.subscribeUserToEvent(301, "user1"));
    }

    @Test
    @Order(10)
    public void subscribeUserToEvent_EventFull() {
        Assertions.assertFalse(eventService.subscribeUserToEvent(30, "user1"));
    }

    @Test
    @Order(10)
    public void subscribeUserToEvent_EventNotApproved() {
        Assertions.assertFalse(eventService.subscribeUserToEvent(40, "user1"));
    }

    @Test
    @Order(10)
    public void subscribeUserToEvent_EventStartBeforeNow() {
        Assertions.assertFalse(eventService.subscribeUserToEvent(4, "user1"));
    }

    @Test
    @Order(11)
    public void subscribeUserToEvent_Success() {
        Assertions.assertTrue(eventService.subscribeUserToEvent(31, "user1"));
    }

    @Test
    @Order(12)
    public void subscribeUserToEvent_UserAlreadySubscribed() {
        Assertions.assertFalse(eventService.subscribeUserToEvent(31, "user1"));
    }

    @Test
    @Order(20)
    public void unsubscribeUserToEvent_InvalidUser() {
        Assertions.assertFalse(eventService.unsubscribeUserToEvent(31, "user101"));
    }

    @Test
    @Order(20)
    public void unsubscribeUserToEvent_InvalidEvent() {
        Assertions.assertFalse(eventService.unsubscribeUserToEvent(310, "user1"));
    }

    @Test
    @Order(20)
    public void unsubscribeUserToEvent_EventNotApproved() {
        Assertions.assertFalse(eventService.unsubscribeUserToEvent(44, "user1"));
    }

    @Test
    @Order(20)
    public void unsubscribeUserToEvent_StartBeforeNow() {
        Assertions.assertFalse(eventService.unsubscribeUserToEvent(10, "user1"));
    }

    @Test
    @Order(21)
    public void unsubscribeUserToEvent_Success() {
        Assertions.assertTrue(eventService.unsubscribeUserToEvent(31, "user1"));
    }

    @Test
    @Order(22)
    public void unsubscribeUserToEvent_UserAlreadyUnsubscribed() {
        Assertions.assertFalse(eventService.unsubscribeUserToEvent(31, "user1"));
    }

    @Test
    @Order(30)
    public void rateEvent_InvalidUsername() {
        Assertions.assertFalse(eventService.rateEvent(14, rateFormDto, "user101"));
    }

    @Test
    @Order(30)
    public void rateEvent_InvalidEvent() {
        Assertions.assertFalse(eventService.rateEvent(140, rateFormDto, "user1"));
    }

    @Test
    @Order(30)
    public void rateEvent_EventNotApproved() {
        Assertions.assertFalse(eventService.rateEvent(1, rateFormDto, "user1"));
    }

    @Test
    @Order(30)
    public void rateEvent_EventNotFinished() {
        Assertions.assertFalse(eventService.rateEvent(32, rateFormDto, "user1"));
    }

    @Test
    @Order(30)
    public void rateEvent_UserNotSubscribed() {
        Assertions.assertFalse(eventService.rateEvent(14, rateFormDto, "user1"));
    }

    @Test
    @Order(31)
    public void rateEvent_Success() {
        Assertions.assertTrue(eventService.rateEvent(12, rateFormDto, "user1"));
    }

    @Test
    @Order(32)
    public void rateEvent_AlreadyRated() {
        Assertions.assertFalse(eventService.rateEvent(12, rateFormDto, "user1"));
    }

    @AfterAll
    @Transactional
    public void destroy() {
        // Delete subscriptions and events
        subscriptionRepository.deleteAll();
        eventRepository.deleteAll();
    }

}