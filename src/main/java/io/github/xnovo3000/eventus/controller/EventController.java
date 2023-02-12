package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.security.JpaUserDetails;
import io.github.xnovo3000.eventus.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String get(
            Model model,
            @AuthenticationPrincipal JpaUserDetails userDetails,
            @PathVariable Long id
    ) {
        // Get event
        return eventService.getById(id)
                .map(eventDto -> {
                    // Check if user can participate
                    boolean canParticipate = eventDto.getHoldings().stream()
                            .noneMatch(it -> it.getUsername().equals(userDetails.getUsername())) &&
                            eventDto.getSeats() > eventDto.getHoldings().size();
                    // Check if user participates and can remove its participation
                    boolean canRemoveParticipation = eventDto.getHoldings()
                            .stream().anyMatch(it -> it.getUsername().equals(userDetails.getUsername()));
                    // Set values
                    model.addAttribute("event", eventDto);
                    model.addAttribute("can_participate", canParticipate);
                    model.addAttribute("can_remove_participation", canRemoveParticipation);
                    // Render HTML
                    return "event";
                })
                .orElseThrow(NoSuchElementException::new);
    }

}