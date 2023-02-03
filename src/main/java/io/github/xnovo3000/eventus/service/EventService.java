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
     * @return The event DTO if it has been created, empty Optional otherwise
     */
    Optional<EventBriefDto> proposeEvent(ProposeEventDtoZoned proposeEventDto, String username);

}