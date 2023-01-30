package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.dto.RegisterFormDto;
import io.github.xnovo3000.eventus.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.regex.Pattern;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final Pattern emailPattern;

    @GetMapping
    public String get() {
        return "register";
    }

    @PostMapping
    public String post(@ModelAttribute RegisterFormDto registerFormDto) {
        // Email pattern check
        if (!emailPattern.matcher(registerFormDto.getEmail()).matches()) {
            return "redirect:/register?invalid_email";
        }
        // Try to create user
        if (userService.registerNewUser(registerFormDto)) {
            return "redirect:/login?register_success";
        } else {
            return "redirect:/register?error";
        }
    }

}