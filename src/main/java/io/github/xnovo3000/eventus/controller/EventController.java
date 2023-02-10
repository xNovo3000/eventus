package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.dto.EventDto;
import io.github.xnovo3000.eventus.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public String get(Model model, @PathVariable Long id) {
        // Get event
        return eventService.getById(id)
                .map(eventDto -> {
                    model.addAttribute("event", eventDto);
                    return "event";
                })
                .orElseThrow(NoSuchElementException::new);
    }

}