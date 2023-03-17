package io.github.xnovo3000.eventus.mvc.service;

import io.github.xnovo3000.eventus.bean.dto.input.RateFormDto;
import io.github.xnovo3000.eventus.bean.dto.output.EventCardDto;
import io.github.xnovo3000.eventus.bean.dto.output.EventDto;
import io.github.xnovo3000.eventus.bean.dto.input.zoned.ProposeEventDtoZoned;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface EventService {

    /**
     * Return an event by its id
     *
     * @param id The id of the event
     * @return The event if it exists, empty otherwise
     */
    Optional<EventDto> getById(Long id);

    /**
     * Get all events that have started but not finished yet
     *
     * @return A list of ongoing events
     */
    List<EventCardDto> getOngoingEvents();

    /**
     * Get a page of events that haven't started yet
     *
     * @param pageNumber The page number of the paginated result
     * @return A page of future events
     */
    Page<EventCardDto> getFutureEvents(int pageNumber);

    /**
     * Get a page of events that are in the 'proposed' state
     *
     * @param pageNumber The page number of the paginated result - starts from 1
     * @return A page of proposed events
     */
    Page<EventCardDto> getProposed(int pageNumber);

    /**
     * Create a new unapproved event. The event start must
     * be before end and after now
     *
     * @param proposeEventDto The event DTO
     * @return The created event id
     */
    Optional<Long> proposeEvent(ProposeEventDtoZoned proposeEventDto);

    /**
     * Approve an event by its id, the event must be non-approved
     * and start must be after now
     *
     * @param eventId The event id
     * @return True if the event can be approved, false otherwise
     */
    boolean approveEvent(Long eventId);

    /**
     * Reject an event by its id, the event must be non-approved
     *
     * @param eventId The event id
     * @return True if the event can be rejected, false otherwise
     */
    boolean rejectEvent(Long eventId);

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
     * @param eventId The event id
     * @param username The user's username
     * @return True if has been subscribed, false otherwise
     */
    boolean subscribeUserToEvent(Long eventId, String username);

    /**
     * Unsubscribe a user to an event, fails if:
     * the username does not exist,
     * the event does not exist,
     * the event is not approved,
     * the user is already unsubscribed
     * the event start is before now
     *
     * @param eventId The event id
     * @param username The user's username
     * @return True if has been unsubscribed, false otherwise
     */
    boolean unsubscribeUserToEvent(Long eventId, String username);

    /**
     * Rate a finished event
     *
     * @param eventId The id of the event
     * @param dto The dto
     * @param username The user that wants to rate the event
     * @return True if the event has been rated, false otherwise
     */
    boolean rateEvent(Long eventId, RateFormDto dto, String username);

}