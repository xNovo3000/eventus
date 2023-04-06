package io.github.xnovo3000.eventus.mvc.service;

import org.jetbrains.annotations.NotNull;

/**
 * Service class that handles all the operations regarding events
 */
public interface EmailService {

    /**
     * Send a password via email.
     * Used for registration and password reset systems
     *
     * @param email The e-mail address to send the password to
     * @param password The password
     */
    void sendPasswordViaEmail(@NotNull String email, @NotNull String password);

}