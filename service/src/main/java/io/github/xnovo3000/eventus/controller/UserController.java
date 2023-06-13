package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.api.dto.input.UpdateAuthoritiesDto;
import io.github.xnovo3000.eventus.api.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Controller that handles '/user'
 */
@Controller
@RequestMapping("/user")
@Validated
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get the user manager page. Requires USER_MANAGER
     *
     * @param model The UI model
     * @param error The error, if exists
     * @param page The request page
     * @param usernameToFind The username to filter
     * @return The page
     */
    @GetMapping
    public String get(
            Model model,
            @RequestAttribute(required = false) String error,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(required = false) String usernameToFind
    ) {
        // Inject error
        model.addAttribute("error", error);
        // Set model
        model.addAttribute("page", page);
        model.addAttribute("username", usernameToFind);
        model.addAttribute("users", userService.getByFilter(page, usernameToFind));
        // Render HTML
        return "page/user";
    }

    /**
     * Disable a user by its ID
     *
     * @param id The user's ID
     * @param referer The page originating the request
     * @param session The user's session
     * @return The redirect page
     */
    @PostMapping("/{id}/disable")
    public String postDisable(
            @PathVariable Long id,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (!userService.disable(id)) {
            session.setAttribute("error", "user_disable_error");
        }
        return String.format("redirect:%s", referer);
    }

    /**
     * Enable a user by its ID
     *
     * @param id The user's ID
     * @param referer The page originating the request
     * @param session The user's session
     * @return The redirect page
     */
    @PostMapping("/{id}/enable")
    public String postEnable(
            @PathVariable Long id,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (!userService.enable(id)) {
            session.setAttribute("error", "user_enable_error");
        }
        return String.format("redirect:%s", referer);
    }

    /**
     * Reset a user's password by its ID
     *
     * @param id The user's ID
     * @param referer The page originating the request
     * @param session The user's session
     * @return The redirect page
     */
    @PostMapping("/{id}/reset_password")
    public String postResetPassword(
            @PathVariable Long id,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (!userService.resetPassword(id)) {
            session.setAttribute("error", "user_reset_password_error");
        }
        return String.format("redirect:%s", referer);
    }

    /**
     * Update user's authorities by its ID
     *
     * @param id The user's ID
     * @param referer The page originating the request
     * @param session The user's session
     * @param dto The user's new authorities
     * @return The redirect page
     */
    @PostMapping("/{id}/update_authorities")
    public String postUpdateAuthorities(
            @PathVariable Long id,
            @RequestHeader String referer,
            @ModelAttribute @Valid UpdateAuthoritiesDto dto,
            HttpSession session
    ) {
        // Create authorities list
        val authorities = new ArrayList<String>();
        if (dto.getUserManager() != null) {
            authorities.add("USER_MANAGER");
        }
        if (dto.getEventManager() != null) {
            authorities.add("EVENT_MANAGER");
        }
        // Update
        if (!userService.updateAuthorities(id, authorities)) {
            session.setAttribute("error", "user_update_authorities_error");
        }
        return String.format("redirect:%s", referer);
    }

}