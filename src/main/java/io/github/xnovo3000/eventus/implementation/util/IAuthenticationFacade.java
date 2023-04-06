package io.github.xnovo3000.eventus.implementation.util;

import io.github.xnovo3000.eventus.security.JpaUserDetails;
import io.github.xnovo3000.eventus.util.AuthenticationFacade;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IAuthenticationFacade implements AuthenticationFacade {

    @Override
    public Optional<JpaUserDetails> getUserDetails() {
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
