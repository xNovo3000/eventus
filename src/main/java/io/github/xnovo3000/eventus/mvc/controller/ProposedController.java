package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.mvc.service.EventService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/proposed_events")
@Validated
@AllArgsConstructor
public class ProposedController {

    private final EventService eventService;

    @GetMapping
    public String get(Model model, @RequestParam(defaultValue = "1") @Min(1) Integer page) {
        // Set model
        model.addAttribute("proposed_event", eventService.getProposed(page));
        model.addAttribute("page", page);
        // Render HTML
        return "page/proposed";
    }

}