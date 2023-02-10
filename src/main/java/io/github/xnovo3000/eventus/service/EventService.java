package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import io.github.xnovo3000.eventus.dto.ProposeEventDtoZoned;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface EventService {

    /**
     * Get all events that have started but not finished yet
     *
     * @return A list of ongoing events
     */
    List<EventBriefDto> getOngoingEvents();

    /**
     * Get a page of events that haven't started yet
     *
     * @param pageNumber The page number of the paginated result
     * @return A page of future events
     */
    Page<EventBriefDto> getFutureEvents(int pageNumber);

    /**
     * Create a new unapproved event
     *
     * @param proposeEventDto The event DTO
     * @param username The username that generated the event
     * @return The created event id
     */
    Optional<Long> proposeEvent(ProposeEventDtoZoned proposeEventDto, String username);

    /**
     * Make a user participate an event
     *
     * @param eventId The id of the event
     * @param username The username of the user generating the request
     * @return True if it can participate, false otherwise
     */
    boolean participateToEvent(Long eventId, String username);

    /**
     * Make a user don't participate an event
     *
     * @param eventId The id of the event
     * @param username The username of the user generating the request
     * @return True if it can remove participation, false otherwise
     */
    boolean dontParticipateToEvent(Long eventId, String username);

}