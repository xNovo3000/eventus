package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import org.springframework.data.domain.Page;

import java.util.List;

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

}