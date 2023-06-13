package io.github.xnovo3000.eventus.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller that handles '/login'
 */
@Controller
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    /**
     * Get the login page
     *
     * @return The login page
     */
    @GetMapping
    public String get() {
        return "page/login";
    }

}