package io.github.xnovo3000.eventus.mvc.service;

public interface EmailService {

    /**
     * Send a password via email.
     * Used for registration and password reset systems
     *
     * @param email The e-mail address to send the password to
     * @param password The password
     */
    void sendPasswordViaEmail(String email, String password);

}