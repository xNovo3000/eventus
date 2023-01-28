package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    List<EventBriefDto> getOngoingEvents();

    Page<EventBriefDto> getFutureEvents(int pageNumber);

}