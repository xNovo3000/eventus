package io.github.xnovo3000.eventus.security;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaAuthenticationProxy {

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