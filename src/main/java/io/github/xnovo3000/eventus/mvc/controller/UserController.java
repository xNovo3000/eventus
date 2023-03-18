package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.mvc.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@Validated
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String get(
            Model model,
            @RequestAttribute(required = false) String error,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(required = false) String username
    ) {
        // Inject error
        model.addAttribute("error", error);
        // Set model
        model.addAttribute("page", page);
        model.addAttribute("username", username);
        model.addAttribute("users", userService.getByFilter(page, username));
        // Render HTML
        return "page/user";
    }

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

    @PostMapping("/{id}/disable")
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

    // TODO: Authority update method

}