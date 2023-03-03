package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.mvc.service.EventService;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public String get(Model model, @PathVariable Long id) {
        return eventService.getById(id)
                .map(eventDto -> {
                    // Set values
                    model.addAttribute("event", eventDto);
                    // Render HTML
                    return "page/event";
                })
                .orElseThrow(NoSuchElementException::new);
        // TODO: Bind NoSuchElementException to 400 Not Found
    }

    @PostMapping("/{id}/subscribe")
    public String postSubscribe(
            @PathVariable Long id,
            @RequestHeader String referer,
            @AuthenticationPrincipal JpaUserDetails userDetails
    ) {
        if (eventService.subscribeUserToEvent(id, userDetails.getUsername())) {
            return String.format("redirect:%s", referer);
        } else {
            return String.format("redirect:%s?subscribe_event_fail", referer);
        }
    }

    @PostMapping("/{id}/unsubscribe")
    public String postUnsubscribe(
            @PathVariable Long id,
            @RequestHeader String referer,
            @AuthenticationPrincipal JpaUserDetails userDetails
    ) {
        if (eventService.unsubscribeUserToEvent(id, userDetails.getUsername())) {
            return String.format("redirect:%s", referer);
        } else {
            return String.format("redirect:%s?unsubscribe_event_fail", referer);
        }
    }

}