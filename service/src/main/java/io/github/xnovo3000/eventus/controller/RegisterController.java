package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.api.dto.input.RegisterFormDto;
import io.github.xnovo3000.eventus.api.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@Validated
@AllArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String get() {
        return "page/register";
    }

    @PostMapping
    public String post(@ModelAttribute @Valid RegisterFormDto registerFormDto) {
        if (userService.registerNewUser(registerFormDto)) {
            return "redirect:/login?register_success";
        } else {
            return "redirect:/register?user_already_registered";
        }
    }

}