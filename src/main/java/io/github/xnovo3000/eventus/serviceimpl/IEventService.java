package io.github.xnovo3000.eventus.serviceimpl;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import io.github.xnovo3000.eventus.repository.EventRepository;
import io.github.xnovo3000.eventus.service.EventService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class IEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IEventService.class);

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final Integer pageSize;

    public IEventService(
            EventRepository eventRepository,
            ModelMapper modelMapper,
            @Value("${io.github.xnovo3000.eventus.page-size}") Integer pageSize
    ) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.pageSize = pageSize;
    }

    @Override
    public List<EventBriefDto> getOngoingEvents() {
        OffsetDateTime now = OffsetDateTime.now();
        return eventRepository.findAllByApprovedIsTrueAndStartIsBeforeAndEndIsAfter(now, now)
                .stream().map(event -> modelMapper.map(event, EventBriefDto.class))
                .toList();
    }

    @Override
    public Page<EventBriefDto> getFutureEvents(int pageNumber) {
        OffsetDateTime now = OffsetDateTime.now();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return eventRepository.findAllByApprovedIsTrueAndStartIsAfter(now, pageable)
                .map(event -> modelMapper.map(event, EventBriefDto.class));
    }

}