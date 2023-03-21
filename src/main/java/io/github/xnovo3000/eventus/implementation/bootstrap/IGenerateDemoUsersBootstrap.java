package io.github.xnovo3000.eventus.implementation.bootstrap;

import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.mvc.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Component
@Profile("default")
@AllArgsConstructor
public class IGenerateDemoUsersBootstrap implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(IGenerateAdminUserBootstrap.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // Create more users to test with
        LOGGER.info("IGenerateDemoUsersBootstrap: running");
        IntStream.range(0, 100)
                .mapToObj(id -> {
                    val newUser = new User();
                    newUser.setUsername("user" + id);
                    newUser.setPassword(passwordEncoder.encode("user" + id));
                    newUser.setEmail("user" + id + "@eventus.com");
                    newUser.setActive(true);
                    return newUser;
                })
                .forEach(user -> {
                    if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
                        try {
                            userRepository.save(user);
                        } catch (Exception e) {
                            LOGGER.error("Failed to save user", e);
                        }
                    }
                });
    }

}