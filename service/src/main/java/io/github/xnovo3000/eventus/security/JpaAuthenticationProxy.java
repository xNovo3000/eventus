package io.github.xnovo3000.eventus.security;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Proxy class to SecurityContextHolder. Useful for JPA
 * auditing and null-safe user details getter
 */
@Component
public class JpaAuthenticationProxy {

    /**
     * Get the current user details
     *
     * @return JpaUserDetails of the current user if present, empty otherwise
     */
    public @NotNull Optional<JpaUserDetails> getUserDetails() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        val principal = authentication.getPrincipal();
        if (!(principal instanceof JpaUserDetails)) {
            return Optional.empty();
        }
        return Optional.of((JpaUserDetails) principal);
    }

}