package io.github.xnovo3000.eventus.bootstrap;

import io.github.xnovo3000.eventus.api.entity.User;
import io.github.xnovo3000.eventus.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

/**
 * Bootstrap runner that implements the command pattern. This function runs only
 * if the current profile of the application is 'default' (e.g.: launched from
 * an IDE) and generates 100 demo users for testing
 */
@Component
@Profile("default")
@AllArgsConstructor
@Slf4j
public class IGenerateDemoUsersBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // Create more users to test with
        log.info("IGenerateDemoUsersBootstrap: running");
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
                            log.error("Failed to save user", e);
                        }
                    }
                });
    }

}