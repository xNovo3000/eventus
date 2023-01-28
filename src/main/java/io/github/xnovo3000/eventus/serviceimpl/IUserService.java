package io.github.xnovo3000.eventus.serviceimpl;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import io.github.xnovo3000.eventus.repository.UserRepository;
import io.github.xnovo3000.eventus.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class IUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IUserService.class);

    private final UserRepository userRepository;

    @Override
    public List<EventBriefDto> getOngoingEvents() {
        // TODO: Implement
        return List.of();
    }

    @Override
    public Page<EventBriefDto> getFutureEvents(int pageNumber) {
        // TODO: Implement
        return new PageImpl<>(List.of());
    }

}