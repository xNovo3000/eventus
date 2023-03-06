package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.bean.dto.input.ProposeEventDto;
import io.github.xnovo3000.eventus.mvc.service.EventService;
import io.github.xnovo3000.eventus.util.DtoMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;

@Controller
@RequestMapping("/")
@Validated
@AllArgsConstructor
public class HomeController {

    private final EventService eventService;
    private final DtoMapper dtoMapper;

    @GetMapping
    public String get(
            Model model,
            @RequestAttribute(required = false) String error,
            @RequestParam(defaultValue = "1") @Min(1) Integer page
    ) {
        // Inject error
        model.addAttribute("error", error);
        // Set model
        model.addAttribute("page", page);
        model.addAttribute("ongoing_events", eventService.getOngoingEvents());
        model.addAttribute("future_events", eventService.getFutureEvents(page));
        // Render HTML
        return "page/home";
    }

    @PostMapping("/propose")
    public String postPropose(
            @ModelAttribute @Valid ProposeEventDto dto,
            @RequestHeader String referer,
            HttpSession session,
            TimeZone timeZone
    ) {
        return eventService.proposeEvent(dtoMapper.toProposeEventDtoZoned(dto, timeZone))
                .map(eventId -> String.format("redirect:/event/%d", eventId))
                .orElseGet(() -> {
                    session.setAttribute("error", "propose_event_error");
                    return String.format("redirect:%s", referer);
                });
    }

}