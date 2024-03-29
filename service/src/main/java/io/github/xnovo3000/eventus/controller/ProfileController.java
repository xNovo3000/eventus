package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.api.dto.input.ChangePasswordDto;
import io.github.xnovo3000.eventus.api.service.EventService;
import io.github.xnovo3000.eventus.api.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;

/**
 * Controller that handles '/profile'
 */
@Controller
@RequestMapping("/profile")
@Validated
@AllArgsConstructor
public class ProfileController {

    private final EventService eventService;
    private final UserService userService;

    /**
     * Get the user's profile
     *
     * @param model The UI model
     * @param page The requested page of event that user attended
     * @param timeZone The timezone of the client
     * @return The page to render
     */
    @GetMapping
    public String get(
            Model model,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            TimeZone timeZone
    ) {
        // Get event history
        model.addAttribute("events", eventService.getEventsThatUserParticipated(page));
        model.addAttribute("page", page);
        model.addAttribute("timezone", timeZone);
        // Render HTML
        return "page/profile";
    }

    /**
     * Get the history with the page
     *
     * @param dto The change password DTO
     * @param referer The page originating the request
     * @param session The user's session
     * @return The page to render
     */
    @PostMapping("/change_password")
    public String postChangePassword(
            @ModelAttribute @Valid ChangePasswordDto dto,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (userService.changePassword(dto)) {
            session.invalidate();
            return "redirect:/login";
        } else {
            session.setAttribute("error", "profile_change_password_error");
            return String.format("redirect:%s", referer);
        }
    }

}