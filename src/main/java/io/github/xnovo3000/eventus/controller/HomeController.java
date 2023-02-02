package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import io.github.xnovo3000.eventus.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

    private final EventService eventService;

    @GetMapping
    public String get(Model model, @RequestParam(defaultValue = "1") Integer page) {
        // Get data
        List<EventBriefDto> ongoingEvents = eventService.getOngoingEvents();
        Page<EventBriefDto> futureEvents = eventService.getFutureEvents(page);
        // Set model
        model.addAttribute("ongoing_events", ongoingEvents);
        model.addAttribute("future_events", futureEvents.getContent());
        model.addAttribute("page", page);
        model.addAttribute("total_pages", futureEvents.getTotalPages());
        model.addAttribute("has_previous", page > 1);
        model.addAttribute("has_next", page < futureEvents.getTotalPages());
        // Render HTML page
        return "home";
    }

}