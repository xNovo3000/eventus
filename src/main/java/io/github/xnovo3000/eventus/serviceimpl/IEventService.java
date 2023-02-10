package io.github.xnovo3000.eventus.serviceimpl;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import io.github.xnovo3000.eventus.dto.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.entity.Event;
import io.github.xnovo3000.eventus.entity.User;
import io.github.xnovo3000.eventus.repository.EventRepository;
import io.github.xnovo3000.eventus.repository.UserRepository;
import io.github.xnovo3000.eventus.service.EventService;
import io.github.xnovo3000.eventus.util.DtoMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IEventService.class);

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;
    private final Integer pageSize;

    public IEventService(
            EventRepository eventRepository,
            DtoMapper dtoMapper,
            UserRepository userRepository,
            @Value("${io.github.xnovo3000.eventus.page-size}") Integer pageSize
    ) {
        this.eventRepository = eventRepository;
        this.dtoMapper = dtoMapper;
        this.userRepository = userRepository;
        this.pageSize = pageSize;
    }

    @Override
    public List<EventBriefDto> getOngoingEvents() {
        OffsetDateTime now = OffsetDateTime.now();
        return eventRepository.findAllByApprovedIsTrueAndStartIsBeforeAndEndIsAfter(now, now)
                .stream().map(dtoMapper::toEventBriefDto)
                .toList();
    }

    @Override
    public Page<EventBriefDto> getFutureEvents(int pageNumber) {
        OffsetDateTime now = OffsetDateTime.now();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return eventRepository.findAllByApprovedIsTrueAndStartIsAfterOrderByStartAsc(now, pageable)
                .map(dtoMapper::toEventBriefDto);
    }

    @Override
    public Optional<EventBriefDto> proposeEvent(ProposeEventDtoZoned proposeEventDto, String username) {
        LOGGER.debug("proposeEvent called with payload: " + proposeEventDto);
        // Ensure start is before end
        if (proposeEventDto.getStart().isAfter(proposeEventDto.getEnd())) {
            LOGGER.debug("Received start after end. Start: " + proposeEventDto.getStart() + ", end: " + proposeEventDto.getEnd());
            return Optional.empty();
        }
        // Get the user with that username
        Optional<User> maybeUser = userRepository.findByUsername(username);
        if (maybeUser.isEmpty()) {
            LOGGER.warn("Username not found: " + username);
            return Optional.empty();
        }
        // Create the event
        Event event = new Event();
        event.setName(proposeEventDto.getName());
        event.setDescription(proposeEventDto.getDescription());
        event.setCreator(maybeUser.get());
        event.setStart(proposeEventDto.getStart());
        event.setEnd(proposeEventDto.getEnd());
        event.setSeats(proposeEventDto.getSeats());
        event.setApproved(false);
        // Insert into the database
        event = eventRepository.save(event);
        // Return success
        return Optional.of(dtoMapper.toEventBriefDto(event));
    }

}