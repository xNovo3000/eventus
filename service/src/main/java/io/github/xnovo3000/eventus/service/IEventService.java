package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.api.dto.input.RateFormDto;
import io.github.xnovo3000.eventus.api.dto.output.EventCardDto;
import io.github.xnovo3000.eventus.api.dto.output.EventDto;
import io.github.xnovo3000.eventus.api.dto.input.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.api.entity.Event;
import io.github.xnovo3000.eventus.api.entity.Subscription;
import io.github.xnovo3000.eventus.api.repository.EventRepository;
import io.github.xnovo3000.eventus.api.repository.SubscriptionRepository;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import io.github.xnovo3000.eventus.api.service.EventService;
import io.github.xnovo3000.eventus.api.util.DtoMapper;
import io.github.xnovo3000.eventus.security.JpaAuthenticationProxy;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * EventService implementation
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class IEventService implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;
    private final SubscriptionRepository subscriptionRepository;
    private final JpaAuthenticationProxy authenticationProxy;

    @Value("${io.github.xnovo3000.eventus.event_page_size}") private Integer pageSize = 12;
    
    @Override
    public @NotNull Optional<EventDto> getById(long id) {
        val username = authenticationProxy.getUserDetails()
                .map(JpaUserDetails::getUsername)
                .orElse(null);
        return eventRepository.findById(id)
                .map(event -> dtoMapper.toEventDto(event, username));
    }

    @Override
    public @NotNull List<EventCardDto> getOngoingEvents() {
        val now = OffsetDateTime.now();
        return eventRepository.findAllByApprovedIsTrueAndStartIsBeforeAndEndIsAfter(now, now)
                .stream().map(dtoMapper::toEventCardDto)
                .toList();
    }

    @Override
    public @NotNull Page<EventCardDto> getFutureEvents(int pageNumber) {
        val now = OffsetDateTime.now();
        val username = authenticationProxy.getUserDetails()
                .map(JpaUserDetails::getUsername)
                .orElse(null);
        val pageable = PageRequest.of(pageNumber - 1, pageSize);
        return eventRepository.findAllByApprovedIsTrueAndStartIsAfterOrderByStartAsc(now, pageable)
                .map(event -> dtoMapper.toEventCardDto(event, username));
    }

    @Override
    public @NotNull Page<EventCardDto> getProposedEvents(int pageNumber) {
        val now = OffsetDateTime.now();
        val pageable = PageRequest.of(pageNumber - 1, pageSize);
        return eventRepository.findAllByApprovedIsFalseAndStartIsAfterOrderByStartAsc(now, pageable)
                .map(dtoMapper::toEventCardDto);
    }

    @Override
    public @NotNull Page<EventCardDto> getHistory(int pageNumber) {
        val pageable = PageRequest.of(pageNumber - 1, pageSize);
        return eventRepository.findAllByOrderByStartDesc(pageable)
                .map(dtoMapper::toEventCardDto);
    }

    @Override
    public @NotNull Page<EventCardDto> getEventsThatUserParticipated(int pageNumber) {
        val username = authenticationProxy.getUserDetails().map(JpaUserDetails::getUsername).orElse(null);
        val pageable = PageRequest.of(pageNumber - 1, pageSize);
        return eventRepository.findAllByHoldings_User_UsernameOrderByStartDesc(username, pageable)
                .map(event -> dtoMapper.toEventCardDto(event, username));
    }

    @Override
    public @NotNull Optional<Long> proposeEvent(@NotNull ProposeEventDtoZoned proposeEventDto) {
        log.info("proposeEvent called with payload: " + proposeEventDto);
        // Ensure start is before end
        if (!proposeEventDto.getStart().isBefore(proposeEventDto.getEnd())) {
            log.info("Event start must be always before end");
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
            log.error("Cannot save event", e);
            return Optional.empty();
        }
    }

    @Override
    public boolean approveEvent(long eventId) {
        log.info("approveEvent called with eventId: " + eventId);
        // Get the event
        val maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            log.info("The event does not exist");
            return false;
        }
        val event = maybeEvent.get();
        // Check if it is already started
        if (event.getStart().isBefore(OffsetDateTime.now())) {
            log.info("This event is already started");
            return false;
        }
        // Check if it is already approved
        if (event.getApproved()) {
            log.info("This event is already approved");
            return false;
        }
        // Set approved and try to save
        event.setApproved(true);
        try {
            eventRepository.save(event);
            return true;
        } catch (Exception e) {
            log.error("Cannot save event", e);
            return false;
        }
    }

    @Override
    public boolean rejectEvent(long eventId) {
        log.info("rejectEvent called with eventId: " + eventId);
        // Get the event
        val maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            log.info("The event does not exist");
            return false;
        }
        val event = maybeEvent.get();
        // Check if it is already approved
        if (event.getApproved()) {
            log.info("This event is already approved");
            return false;
        }
        // Try to delete
        try {
            eventRepository.delete(event);
            return true;
        } catch (Exception e) {
            log.error("Cannot save event", e);
            return false;
        }
    }

    @Override
    public boolean subscribeUserToEvent(long eventId, @NotNull String username) {
        log.info("subscribeUserToEvent called with eventId: " + eventId + " and username: " + username);
        // Get the event
        val maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            log.info("Event not found");
            return false;
        }
        Event event = maybeEvent.get();
        // Get the user
        val maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isEmpty()) {
            log.info("User not found");
            return false;
        }
        val user = maybeUser.get();
        // The event must be approved
        if (!event.getApproved()) {
            log.info("This event is not approved");
            return false;
        }
        // Check if it is already started
        if (event.getStart().isBefore(OffsetDateTime.now())) {
            log.info("This event is already started");
            return false;
        }
        // The event must not be full
        if (event.getHoldings().size() >= event.getSeats()) {
            log.info("This event is full");
            return false;
        }
        // Check if already subscribed
        if (event.getHoldings().stream().anyMatch(s -> s.getUser().getUsername().equals(username))) {
            log.info("User already subscribed");
            return false;
        }
        // Check if user is already subscribed to an event that collides with
        val overlappingEvents = user.getHoldings().stream()
                .map(Subscription::getEvent)
                .filter(it -> it.getStart().isBefore(event.getEnd()) && it.getEnd().isAfter(event.getStart()))
                .toList();
        if (overlappingEvents.size() > 0) {
            log.info("Event overlaps with: " + overlappingEvents);
            return false;
        }
        // Create the subscription bean
        val subscription = new Subscription();
        subscription.setUser(user);
        subscription.setEvent(event);
        // Try to insert into the datasource
        try {
            subscriptionRepository.save(subscription);
            return true;
        } catch (Exception e) {
            log.error("Cannot save subscription", e);
            return false;
        }
    }

    @Override
    public boolean unsubscribeUserToEvent(long eventId, @NotNull String username) {
        log.info("unsubscribeUserToEvent called with eventId: " + eventId + " and username: " + username);
        // Get the event
        val maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            log.info("Event not found");
            return false;
        }
        Event event = maybeEvent.get();
        // Get the user
        val maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isEmpty()) {
            log.info("User not found");
            return false;
        }
        val user = maybeUser.get();
        // The event must be approved
        if (!event.getApproved()) {
            log.info("This event is not approved");
            return false;
        }
        // Check if it is already started
        if (event.getStart().isBefore(OffsetDateTime.now())) {
            log.info("This event is already started");
            return false;
        }
        // Get the subscription bean
        val maybeSubscription = subscriptionRepository.findByUserAndEvent(user, event);
        if (maybeSubscription.isEmpty()) {
            log.info("User is not subscribed to the event");
            return false;
        }
        val subscription = maybeSubscription.get();
        // Try to delete
        try {
            subscriptionRepository.delete(subscription);
            return true;
        } catch (Exception e) {
            log.error("Cannot delete subscription", e);
            return false;
        }
    }

    @Override
    public boolean rateEvent(long eventId, @NotNull RateFormDto dto, @NotNull String username) {
        log.info("rateEvent called with eventId: " + eventId + ", payload: " + dto + " and username: " + username);
        // Get the event
        val maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            log.info("Event not found");
            return false;
        }
        Event event = maybeEvent.get();
        // Check if the event is terminated. End must be before now
        val now = OffsetDateTime.now();
        if (!(event.getEnd().isBefore(now))) {
            log.info("Event is not finished");
            return false;
        }
        // Get the user
        val maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isEmpty()) {
            log.info("User not found");
            return false;
        }
        val user = maybeUser.get();
        // Get subscription
        val maybeSubscription = subscriptionRepository.findByUserAndEvent(user, event);
        if (maybeSubscription.isEmpty()) {
            log.info("Subscription not found");
            return false;
        }
        val subscription = maybeSubscription.get();
        // Check if the event has already been rated
        if (subscription.getRating() != null || subscription.getComment() != null) {
            log.info("The event has already been rated");
            return false;
        }
        // Rate the comment
        subscription.setRating(dto.getStars());
        subscription.setComment(dto.getComment());
        // Save in the database
        try {
            subscriptionRepository.save(subscription);
            return true;
        } catch (Exception e) {
            log.error("Cannot save subscription", e);
            return false;
        }
    }

    @Override
    public boolean deleteOldUnapprovedEvents() {
        log.info("deleteOldUnapprovedEvents called");
        // Try to remove
        try {
            eventRepository.deleteAllByApprovedIsFalseAndStartIsBefore(OffsetDateTime.now());
            return true;
        } catch (Exception e) {
            log.error("Cannot delete in batch", e);
            return false;
        }
    }

}