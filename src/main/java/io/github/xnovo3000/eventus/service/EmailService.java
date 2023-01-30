package io.github.xnovo3000.eventus.service;

public interface EmailService {
    void sendPasswordViaEmail(String email, String password);
}