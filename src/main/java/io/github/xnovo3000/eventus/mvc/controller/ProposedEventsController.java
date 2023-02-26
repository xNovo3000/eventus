package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.bean.dto.EventBriefDto;
import io.github.xnovo3000.eventus.mvc.service.EventService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
public class ProposedEventsController {

    private final EventService eventService;

    @GetMapping
    public String get(Model model, @RequestParam(defaultValue = "1") @Min(1) Integer page) {
        // Get data
        Page<EventBriefDto> proposedEvents = eventService.getProposed(page);
        // Set model
        model.addAttribute("proposed_event", proposedEvents);
        model.addAttribute("page", page);
        model.addAttribute("total_pages", Math.max(1, proposedEvents.getTotalPages()));
        model.addAttribute("has_previous", page > 1);
        model.addAttribute("has_next", page < proposedEvents.getTotalPages());
        // Render HTML
        return "proposed_events";
    }

}