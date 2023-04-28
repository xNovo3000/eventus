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

@Controller
@RequestMapping("/profile")
@Validated
@AllArgsConstructor
public class ProfileController {

    private final EventService eventService;
    private final UserService userService;

    @GetMapping
    public String get(
            Model model,
            @RequestParam(defaultValue = "1") @Min(1) Integer page
    ) {
        // Get event history
        model.addAttribute("events", eventService.getEventsThatUserParticipated(page));
        model.addAttribute("page", page);
        // Render HTML
        return "page/profile";
    }

    @PostMapping("/change_password")
    public String postChangePassword(
            @ModelAttribute @Valid ChangePasswordDto dto,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (userService.changePassword(dto)) {
            return "redirect:login";
        } else {
            session.setAttribute("error", "profile_change_password_error");
            return String.format("redirect:%s", referer);
        }
    }

}