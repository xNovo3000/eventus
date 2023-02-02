package io.github.xnovo3000.eventus.serviceimpl;

import io.github.xnovo3000.eventus.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("deploy")
@AllArgsConstructor
public class IEmailServiceDeploy implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendPasswordViaEmail(String email, String password) {
        // Create message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@eventus.com");  // TODO: Check if working on test deploy
        message.setTo(email);
        message.setSubject("Password piattaforma Eventus");
        message.setText("Password per la piattaforma Eventus: " + password);
        // Send message. Will throw if it cannot send email
        javaMailSender.send(message);
    }

}