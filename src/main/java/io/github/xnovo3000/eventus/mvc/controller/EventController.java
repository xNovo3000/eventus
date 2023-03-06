package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.bean.dto.input.RateFormDto;
import io.github.xnovo3000.eventus.mvc.service.EventService;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/event")
@Validated
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public String get(
            Model model,
            @RequestAttribute(required = false) String error,
            @PathVariable Long id
    ) {
        // Inject error
        model.addAttribute("error", error);
        // Add to the attributes if present
        eventService.getById(id)
                .ifPresent(eventDto -> model.addAttribute("event", eventDto));
        // Render HTML
        return "page/event";
    }

    @PostMapping("/{id}/subscribe")
    public String postSubscribe(
            @PathVariable Long id,
            @RequestHeader String referer,
            @AuthenticationPrincipal JpaUserDetails userDetails,
            HttpSession session
    ) {
        if (!eventService.subscribeUserToEvent(id, userDetails.getUsername())) {
            session.setAttribute("error", "subscribe_event_fail");
        }
        return String.format("redirect:%s", referer);
    }

    @PostMapping("/{id}/unsubscribe")
    public String postUnsubscribe(
            @PathVariable Long id,
            @RequestHeader String referer,
            @AuthenticationPrincipal JpaUserDetails userDetails,
            HttpSession session
    ) {
        if (!eventService.unsubscribeUserToEvent(id, userDetails.getUsername())) {
            session.setAttribute("error", "unsubscribe_event_fail");
        }
        return String.format("redirect:%s", referer);
    }

    @PostMapping("/{id}/rate")
    public String postRate(
            @PathVariable Long id,
            @RequestHeader String referer,
            @AuthenticationPrincipal JpaUserDetails userDetails,
            @ModelAttribute @Valid RateFormDto dto,
            HttpSession session
    ) {
        if (!eventService.rateEvent(id, dto, userDetails.getUsername())) {
            session.setAttribute("error", "error_rate_event");
        }
        return String.format("redirect:%s", referer);
    }

}