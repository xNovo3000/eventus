package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.api.dto.input.ProposeEventDto;
import io.github.xnovo3000.eventus.api.dto.input.RateFormDto;
import io.github.xnovo3000.eventus.api.dto.input.RemoveSubscriptionDto;
import io.github.xnovo3000.eventus.api.service.EventService;
import io.github.xnovo3000.eventus.api.util.DtoMapper;
import io.github.xnovo3000.eventus.exception.ResourceNotFoundException;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;

@Controller
@RequestMapping("/event")
@Validated
@AllArgsConstructor
public class EventController {

    private final EventService eventService;
    private final DtoMapper dtoMapper;

    @GetMapping("/{id}")
    public String get(
            Model model,
            @RequestAttribute(required = false) String error,
            @PathVariable Long id
    ) {
        // Inject error
        model.addAttribute("error", error);
        // Add to the attributes if present, throw 404 if not found
        val eventDto = eventService.getById(id).orElseThrow(ResourceNotFoundException::new);
        model.addAttribute("event", eventDto);
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

    @PostMapping("/{id}/approve")
    public String postApprove(
            @PathVariable Long id,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (!eventService.approveEvent(id)) {
            session.setAttribute("error", "approve_reject_error");
        }
        return String.format("redirect:%s", referer);
    }

    @PostMapping("/{id}/reject")
    public String postReject(
            @PathVariable Long id,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (!eventService.rejectEvent(id)) {
            session.setAttribute("error", "approve_reject_error");
        }
        return String.format("redirect:%s", referer);
    }

    @PostMapping("/{id}/remove_subscription")
    public String postRemoveSubscription(
            @ModelAttribute @Valid RemoveSubscriptionDto dto,
            @PathVariable Long id,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (!eventService.unsubscribeUserToEvent(id, dto.getUsername())) {
            session.setAttribute("error", "remove_user_subscription_fail");
        }
        return String.format("redirect:%s", referer);
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