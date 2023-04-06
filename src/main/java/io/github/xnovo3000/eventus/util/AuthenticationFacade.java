package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.security.JpaUserDetails;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Facade used to get the current logged user with null safety
 */
public interface AuthenticationFacade {

    /**
     * Get the current user details
     *
     * @return The user details if an user is logged, empty otherwise
     */
    @NotNull Optional<JpaUserDetails> getUserDetails();

}