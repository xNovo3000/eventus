package io.github.xnovo3000.eventus.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * Facade used to get the current logged user with null safety
 */
public interface AuthenticationProxy<T extends UserDetails> {

    /**
     * Get the current user details
     *
     * @return The user details if a user is logged, empty otherwise
     */
    @NotNull Optional<T> getUserDetails();

}