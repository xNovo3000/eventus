package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.bean.entity.User;

import java.util.Optional;

public interface AuthenticationAdapter {
    String getUsername();
    Optional<User> getAuthenticatedUser();
}