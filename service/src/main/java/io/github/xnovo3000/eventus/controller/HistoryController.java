package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.api.service.EventService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.TimeZone;

/**
 * Controller that handles '/history'
 */
@Controller
@RequestMapping("/history")
@Validated
@AllArgsConstructor
public class HistoryController {

    private final EventService eventService;

    /**
     * Get the history with the page
     *
     * @param model The UI model
     * @param page The requested page
     * @param timeZone The timezone of the client
     * @return The page to render
     */
    @GetMapping
    public String get(
            Model model,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            TimeZone timeZone
    ) {
        // Get event history
        model.addAttribute("events", eventService.getHistory(page));
        model.addAttribute("timezone", timeZone);
        model.addAttribute("page", page);
        // Render HTML
        return "page/history";
    }

}