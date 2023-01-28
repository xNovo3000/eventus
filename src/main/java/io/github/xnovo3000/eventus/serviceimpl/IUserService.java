package io.github.xnovo3000.eventus.serviceimpl;

import io.github.xnovo3000.eventus.repository.UserRepository;
import io.github.xnovo3000.eventus.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class IUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IUserService.class);

    private final UserRepository userRepository;

}