package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.api.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * EmailService implementation on 'default' profile
 */
@Service
@Profile("default")
@Slf4j
public class IEmailServiceDefault implements EmailService {

    @Override
    public void sendPasswordViaEmail(@NotNull String email, @NotNull String password) {
        // Log password
        log.info("Password for '" + email + "': '" + password + "'");
    }

}