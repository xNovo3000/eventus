package io.github.xnovo3000.eventus.implementation.util;

import io.github.xnovo3000.eventus.util.AuthenticationFacade;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class IAuthenticationFacade implements AuthenticationFacade {

    @Override
    public String getUsername() {
        val principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return null;
        }
    }

}
