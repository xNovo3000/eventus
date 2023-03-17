package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.mvc.service.UserService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
@Validated
@AllArgsConstructor
public class UsersController {

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
        return "page/users";
    }

}