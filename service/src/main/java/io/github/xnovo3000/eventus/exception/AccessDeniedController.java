package io.github.xnovo3000.eventus.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/access_denied")
public class AccessDeniedController {

    @GetMapping
    public String get() {
        return "page/access_denied";
    }

}