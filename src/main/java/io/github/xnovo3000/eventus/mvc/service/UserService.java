package io.github.xnovo3000.eventus.mvc.service;

import io.github.xnovo3000.eventus.bean.dto.input.RegisterFormDto;
import io.github.xnovo3000.eventus.bean.dto.output.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    /**
     * Register a new user
     *
     * @param registerFormDto The payload
     * @return True if the user has been created, false if email and/or username is already taken
     */
    boolean registerNewUser(RegisterFormDto registerFormDto);

    /**
     * Get a page of users by page number and maybe the username. If the
     * username is null, no filter regarding username is done.
     *
     * @param pageNumber The page number
     * @param username The username to filter, if not null
     * @return A page of filtered users
     */
    Page<UserDto> getByFilter(int pageNumber, String username);

    /**
     * Disable a user by its ID
     *
     * @param userId The user ID
     * @return True if disabled, false otherwise
     */
    boolean disable(Long userId);

    /**
     * Enable a user by its ID
     *
     * @param userId The user ID
     * @return True if enabled, false otherwise
     */
    boolean enable(Long userId);

    /**
     * Reset user's password by its ID
     *
     * @param userId The user ID
     * @return True if resetted, false otherwise
     */
    boolean resetPassword(Long userId);

    /**
     * Override user's authorities by its id and new authorities
     *
     * @param userId The user ID
     * @param authorities The new authorities
     * @return True if authorities override success, false otherwise
     */
    boolean updateAuthorities(Long userId, List<String> authorities);

}