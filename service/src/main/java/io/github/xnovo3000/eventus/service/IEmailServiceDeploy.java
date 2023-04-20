package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile("deploy")
@RequiredArgsConstructor
public class IEmailServiceDeploy implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String username;

    @Override
    public void sendPasswordViaEmail(@NotNull String email, @NotNull String password) {
        try {
            // Create message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(username);
            message.setTo(email);
            message.setSubject("Password piattaforma Eventus");
            message.setText("Password per la piattaforma Eventus: " + password);
            // Send message. Will throw if it cannot send email
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Cannot send email", e);
            throw e;
        }
    }

}