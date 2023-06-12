package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.api.service.EventService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;

/**
 * Controller that handles '/proposed'
 */
@Controller
@RequestMapping("/proposed")
@AllArgsConstructor
public class ProposedController {

    private final EventService eventService;

    /**
     * Get the proposed events. EVENT_MANAGER is required
     *
     * @param model The UI model
     * @param error The error attribute, if exists
     * @param page The requested page
     * @param timeZone The timezone of the client
     * @return The page to render
     */
    @GetMapping
    public String get(
            Model model,
            @RequestAttribute(required = false) String error,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            TimeZone timeZone
    ) {
        // Inject error
        model.addAttribute("error", error);
        // Set model
        model.addAttribute("proposed_event", eventService.getProposedEvents(page));
        model.addAttribute("page", page);
        // Set timezone
        model.addAttribute("timezone", timeZone);
        // Render HTML
        return "page/proposed";
    }

}