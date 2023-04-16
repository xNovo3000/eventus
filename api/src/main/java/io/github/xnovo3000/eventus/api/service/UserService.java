package io.github.xnovo3000.eventus.api.service;

import io.github.xnovo3000.eventus.api.dto.input.ChangePasswordDto;
import io.github.xnovo3000.eventus.api.dto.input.RegisterFormDto;
import io.github.xnovo3000.eventus.api.dto.output.UserDto;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service class that handles all the operations regarding users
 */
public interface UserService {

    /**
     * Register a new user
     *
     * @param registerFormDto The payload
     * @return True if the user has been created, false if email and/or username is already taken
     */
    boolean registerNewUser(@NotNull RegisterFormDto registerFormDto);

    /**
     * Get a page of users by page. If the username is null, no filter regarding username is done.
     * Otherwise, the query will be filtered using 'LIKE %${username}%'
     *
     * @param pageNumber The page number
     * @param username The username to filter, if not null
     * @return A page of filtered users
     */
    @NotNull Page<UserDto> getByFilter(int pageNumber, @Nullable String username);

    /**
     * Disable a user by its ID
     *
     * @param userId The user ID
     * @return True if disabled, false otherwise
     */
    boolean disable(long userId);

    /**
     * Enable a user by its ID
     *
     * @param userId The user ID
     * @return True if enabled, false otherwise
     */
    boolean enable(long userId);

    /**
     * Reset user's password by its ID. The password is generated from
     * the system and sent via email or displayed in some ways
     *
     * @param userId The user ID
     * @return True if success, false otherwise
     */
    boolean resetPassword(long userId);

    /**
     * Override user's authorities by its id and new authorities
     *
     * @param userId The user ID
     * @param authorities The new authorities
     * @return True if authorities override success, false otherwise
     */
    boolean updateAuthorities(long userId, @NotNull List<String> authorities);

    /**
     * Change password for the current logged user
     *
     * @param dto The old and new password
     * @return True if changed, false otherwise
     */
    boolean changePassword(@NotNull ChangePasswordDto dto);

}