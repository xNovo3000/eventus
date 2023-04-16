package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.api.service.EventService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@Validated
@AllArgsConstructor
public class HomeController {

    private final EventService eventService;

    @GetMapping
    public String get(
            Model model,
            @RequestAttribute(required = false) String error,
            @RequestParam(defaultValue = "1") @Min(1) Integer page
    ) {
        // Inject error
        model.addAttribute("error", error);
        // Set model
        model.addAttribute("page", page);
        model.addAttribute("ongoing_events", eventService.getOngoingEvents());
        model.addAttribute("future_events", eventService.getFutureEvents(page));
        // Render HTML
        return "page/home";
    }

}