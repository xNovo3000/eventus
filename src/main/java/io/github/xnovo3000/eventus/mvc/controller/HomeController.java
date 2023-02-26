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

import java.util.List;

@Controller
@RequestMapping("/")
@Validated
@AllArgsConstructor
public class HomeController {

    private final EventService eventService;

    @GetMapping
    public String get(Model model, @RequestParam(defaultValue = "1") @Min(1) Integer page) {
        // Get data
        List<EventBriefDto> ongoingEvents = eventService.getOngoingEvents();
        Page<EventBriefDto> futureEvents = eventService.getFutureEvents(page);
        // Set model
        model.addAttribute("ongoing_events", ongoingEvents);
        model.addAttribute("future_events", futureEvents.getContent());
        model.addAttribute("page", page);
        model.addAttribute("total_pages", Math.max(1, futureEvents.getTotalPages()));
        model.addAttribute("has_previous", page > 1);
        model.addAttribute("has_next", page < futureEvents.getTotalPages());
        // Render HTML page
        return "home";
    }

}