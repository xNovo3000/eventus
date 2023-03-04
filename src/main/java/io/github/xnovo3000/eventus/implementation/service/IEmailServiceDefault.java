package io.github.xnovo3000.eventus.implementation.service;

import io.github.xnovo3000.eventus.mvc.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class IEmailServiceDefault implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IEmailServiceDefault.class);

    @Override
    public void sendPasswordViaEmail(String email, String password) {
        // Log password
        LOGGER.info("Password for '" + email + "': '" + password + "'");
    }

}