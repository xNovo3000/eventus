package io.github.xnovo3000.eventus.mvc.service;

import io.github.xnovo3000.eventus.bean.dto.input.RateFormDto;
import io.github.xnovo3000.eventus.bean.dto.output.EventCardDto;
import io.github.xnovo3000.eventus.bean.dto.output.EventDto;
import io.github.xnovo3000.eventus.bean.dto.input.zoned.ProposeEventDtoZoned;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Service class that handles all the operations regarding events
 */
public interface EventService {

    /**
     * Return an event by its ID
     *
     * @param id The ID of the event
     * @return The event if it exists, empty otherwise
     */
    @NotNull Optional<EventDto> getById(long id);

    /**
     * Get all events that started but not finished yet, ordered by start date descending
     *
     * @return A list of ongoing events
     */
    @NotNull List<EventCardDto> getOngoingEvents();

    /**
     * Get a page of events that haven't started yet, ordered by start date descending
     *
     * @param pageNumber The page number of the paginated result - starts from 1
     * @return A page of future events
     */
    @NotNull Page<EventCardDto> getFutureEvents(int pageNumber);

    /**
     * Get a page of events that are in the 'proposed' state
     * (not started but not accepted from an EVENT_MASTER),
     * ordered by start date descending
     *
     * @param pageNumber The page number of the paginated result - starts from 1
     * @return A page of proposed events
     */
    @NotNull Page<EventCardDto> getProposed(int pageNumber);

    /**
     * Get all events ordered by start date descending
     *
     * @param pageNumber The page number of the paginated result - starts from 1
     * @return A page of events
     */
    @NotNull Page<EventCardDto> getHistory(int pageNumber);

    /**
     * Get all events that user has participated ordered by start date descending
     *
     * @param pageNumber The page number of the paginated result - starts from 1
     * @return A page of events
     */
    @NotNull Page<EventCardDto> getEventsThatUserParticipated(int pageNumber);

    /**
     * Create a new unapproved event. The event start must
     * be before end and after now
     *
     * @param proposeEventDto The event DTO
     * @return The created event ID
     */
    @NotNull Optional<Long> proposeEvent(@NotNull ProposeEventDtoZoned proposeEventDto);

    /**
     * Approve an event by its ID, the event must be non-approved
     * and start must be after now
     *
     * @param eventId The event ID
     * @return True if the event can be approved, false otherwise
     */
    boolean approveEvent(long eventId);

    /**
     * Reject an event by its id, the event must be non-approved
     *
     * @param eventId The event id
     * @return True if the event can be rejected, false otherwise
     */
    boolean rejectEvent(long eventId);

    /**
     * Subscribe a user to an event, fail if:
     * the username does not exist,
     * the event does not exist,
     * the event is full,
     * the event is not approved,
     * the user is already subscribed,
     * the event start is before now,
     * user is subscribed to another event with same time
     *
     * @param eventId The event ID
     * @param username The user's username
     * @return True if has been subscribed, false otherwise
     */
    boolean subscribeUserToEvent(long eventId, @NotNull String username);

    /**
     * Unsubscribe a user to an event, fails if:
     * the username does not exist,
     * the event does not exist,
     * the event is not approved,
     * the user is already unsubscribed
     * the event start is before now
     *
     * @param eventId The event ID
     * @param username The user's username
     * @return True if has been unsubscribed, false otherwise
     */
    boolean unsubscribeUserToEvent(long eventId, @NotNull String username);

    /**
     * Rate a finished event
     *
     * @param eventId The ID of the event
     * @param dto The dto
     * @param username The user that wants to rate the event
     * @return True if the event has been rated, false otherwise
     */
    boolean rateEvent(long eventId, @NotNull RateFormDto dto, @NotNull String username);

}