package io.github.xnovo3000.eventus.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/action")
@Validated
@AllArgsConstructor
public class ActionController {

    @PostMapping("/create_event")
    public String createEvent() {
        return "";
    }

}