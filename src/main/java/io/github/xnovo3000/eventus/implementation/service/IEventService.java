package io.github.xnovo3000.eventus.implementation.service;

import io.github.xnovo3000.eventus.bean.dto.output.EventCardDto;
import io.github.xnovo3000.eventus.bean.dto.output.EventDto;
import io.github.xnovo3000.eventus.bean.dto.input.zoned.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.bean.entity.Event;
import io.github.xnovo3000.eventus.bean.entity.Subscription;
import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.mvc.repository.EventRepository;
import io.github.xnovo3000.eventus.mvc.repository.SubscriptionRepository;
import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
import io.github.xnovo3000.eventus.mvc.service.EventService;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import io.github.xnovo3000.eventus.util.AuthenticationAdapter;
import io.github.xnovo3000.eventus.util.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class IEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IEventService.class);

    private final int PAGE_SIZE = 12;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;
    private final SubscriptionRepository subscriptionRepository;
    private final AuthenticationAdapter authenticationAdapter;

    @Override
    public Optional<EventDto> getById(Long id) {
        val username = authenticationAdapter.getUserDetails()
                .map(JpaUserDetails::getUsername)
                .orElse(null);
        return eventRepository.findById(id)
                .map(event -> dtoMapper.toEventDto(event, username));
    }

    @Override
    public List<EventCardDto> getOngoingEvents() {
        val now = OffsetDateTime.now();
        return eventRepository.findAllByApprovedIsTrueAndStartIsBeforeAndEndIsAfter(now, now)
                .stream().map(dtoMapper::toEventCardDto)
                .toList();
    }

    @Override
    public Page<EventCardDto> getFutureEvents(int pageNumber) {
        val now = OffsetDateTime.now();
        val username = authenticationAdapter.getUserDetails()
                .map(JpaUserDetails::getUsername)
                .orElse(null);
        val pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        return eventRepository.findAllByApprovedIsTrueAndStartIsAfterOrderByStartAsc(now, pageable)
                .map(event -> dtoMapper.toEventCardDto(event, username));
    }

    @Override
    public Page<EventCardDto> getProposed(int pageNumber) {
        val now = OffsetDateTime.now();
        val pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        return eventRepository.findAllByApprovedIsFalseAndStartIsAfterOrderByStartAsc(now, pageable)
                .map(dtoMapper::toEventCardDto);
    }

    @Override
    public Optional<Long> proposeEvent(ProposeEventDtoZoned proposeEventDto) {
        LOGGER.info("proposeEvent called with payload: " + proposeEventDto);
        // Ensure start is before end
        if (!proposeEventDto.getStart().isBefore(proposeEventDto.getEnd())) {
            LOGGER.info("Start is after end. Start: " + proposeEventDto.getStart() + ", end: " + proposeEventDto.getEnd());
            return Optional.empty();
        }
        // Create the event
        Event event = new Event();
        event.setName(proposeEventDto.getName());
        event.setDescription(proposeEventDto.getDescription());
        event.setStart(proposeEventDto.getStart());
        event.setEnd(proposeEventDto.getEnd());
        event.setSeats(proposeEventDto.getSeats());
        event.setApproved(false);
        // Try to insert into the database
        try {
            event = eventRepository.save(event);
            return Optional.of(event.getId());
        } catch (Exception e) {
            LOGGER.error("Cannot save event", e);
            return Optional.empty();
        }
    }

    @Override
    public boolean approveEvent(Long eventId) {
        LOGGER.info("approveEvent called with eventId: " + eventId);
        // Get the event
        val maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            LOGGER.info("The event does not exist");
            return false;
        }
        val event = maybeEvent.get();
        // Check if it's already approved
        if (event.getApproved()) {
            LOGGER.info("Event already approved");
            return false;
        }
        // Check if start is before now
        val now = OffsetDateTime.now();
        if (event.getStart().isBefore(now)) {
            LOGGER.info("Event start before now");
            return false;
        }
        // Set approved and try to save
        event.setApproved(true);
        try {
            eventRepository.save(event);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cannot save event", e);
            return false;
        }
    }

    @Override
    public boolean rejectEvent(Long eventId) {
        LOGGER.info("rejectEvent called with eventId: " + eventId);
        // Get the event
        val maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            LOGGER.info("The event does not exist");
            return false;
        }
        val event = maybeEvent.get();
        // Check if it's already approved
        if (event.getApproved()) {
            LOGGER.info("Event already approved");
            return false;
        }
        // Set approved and try to save
        event.setApproved(false);
        try {
            eventRepository.save(event);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cannot save event", e);
            return false;
        }
    }

    @Override
    public boolean subscribeUserToEvent(Long eventId, String username) {
        LOGGER.info("subscribeUserToEvent called with eventId: " + eventId + " and username: " + username);
        // Get the event
        Optional<Event> maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            LOGGER.info("Event not found");
            return false;
        }
        Event event = maybeEvent.get();
        // Check if there is at least one seat available
        if (event.getSeats() <= event.getHoldings().size()) {
            LOGGER.info("Event is full");
            return false;
        }
        // Check if the event is approved
        if (!event.getApproved()) {
            LOGGER.info("Event is not approved");
            return false;
        }
        // Check for valid date
        val now = OffsetDateTime.now();
        if (!event.getStart().isAfter(now)) {
            LOGGER.info("Event start is before now, cannot modify subscriptions");
            return false;
        }
        // Check if already subscribed
        if (event.getHoldings().stream().anyMatch(s -> s.getUser().getUsername().equals(username))) {
            LOGGER.info("User already subscribed");
            return false;
        }
        // Get the user
        Optional<User> maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isEmpty()) {
            LOGGER.info("User not found");
            return false;
        }
        User user = maybeUser.get();
        // Create the subscription bean
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setEvent(event);
        // Try to insert into the datasource
        try {
            subscriptionRepository.save(subscription);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cannot save subscription", e);
            return false;
        }
    }

    @Override
    public boolean unsubscribeUserToEvent(Long eventId, String username) {
        LOGGER.info("unsubscribeUserToEvent called with eventId: " + eventId + " and username: " + username);
        // Get the event
        Optional<Event> maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            LOGGER.info("Event not found");
            return false;
        }
        Event event = maybeEvent.get();
        // Check if the event is approved
        if (!event.getApproved()) {
            LOGGER.info("Event is not approved");
            return false;
        }
        // Check for valid date
        val now = OffsetDateTime.now();
        if (!event.getStart().isAfter(now)) {
            LOGGER.info("Event start is before now, cannot modify subscriptions");
            return false;
        }
        // Get the user
        Optional<User> maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isEmpty()) {
            LOGGER.info("User not found");
            return false;
        }
        User user = maybeUser.get();
        // Get the subscription bean
        Optional<Subscription> maybeSubscription = subscriptionRepository.findByUserAndEvent(user, event);
        if (maybeSubscription.isEmpty()) {
            LOGGER.info("User is not subscribed to the event");
            return false;
        }
        Subscription subscription = maybeSubscription.get();
        // Try to delete
        try {
            subscriptionRepository.delete(subscription);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cannot delete participation", e);
            return false;
        }
    }

}