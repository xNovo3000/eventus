package io.github.xnovo3000.eventus.mvc.service;

import io.github.xnovo3000.eventus.bean.dto.EventBriefDto;
import io.github.xnovo3000.eventus.bean.dto.EventCardDto;
import io.github.xnovo3000.eventus.bean.dto.EventDto;
import io.github.xnovo3000.eventus.bean.dto.ProposeEventDtoZoned;
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
     * @param pageNumber The page number of the paginated result
     * @return A page of proposed events
     */
    Page<EventCardDto> getProposed(int pageNumber);

    /**
     * Create a new unapproved event
     *
     * @param proposeEventDto The event DTO
     * @param username The username that generated the event
     * @return The created event id
     */
    Optional<Long> proposeEvent(ProposeEventDtoZoned proposeEventDto, String username);

    /**
     * Set the participation to an event
     *
     * @param eventId The id of the event
     * @param username The username of the user generating the request
     * @param value true if user wants to participate, false otherwise
     * @return True if it can set participation, false otherwise
     */
    boolean setParticipationToEvent(Long eventId, String username, boolean value);

    /**
     * Approve or reject an event
     *
     * @param eventId The event id
     * @return True if approved/rejected, false if error
     */
    boolean approveOrRejectEvent(Long eventId, boolean approve);

}