package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.api.dto.input.ProposeEventDto;
import io.github.xnovo3000.eventus.api.dto.input.RateFormDto;
import io.github.xnovo3000.eventus.api.dto.input.RemoveSubscriptionDto;
import io.github.xnovo3000.eventus.api.service.EventService;
import io.github.xnovo3000.eventus.api.util.DtoMapper;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;

/**
 * Controller that handles '/event'
 */
@Controller
@RequestMapping("/event")
@Validated
@AllArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;
    private final DtoMapper dtoMapper;

    /**
     * Display an event by its ID, if exists
     *
     * @param model The UI model
     * @param error The error attribute, if exists
     * @param id The event ID
     * @param timeZone The timezone of the client
     * @return The page to render
     */
    @GetMapping("/{id}")
    public String get(
            Model model,
            @RequestAttribute(required = false) String error,
            @PathVariable Long id,
            TimeZone timeZone
    ) {
        // Inject error
        model.addAttribute("error", error);
        // Add to the attributes if present, throw 404 if not found
        return eventService.getById(id)
                .map((eventDto) -> {
                    // Inject event and client timezone
                    model.addAttribute("event", eventDto);
                    model.addAttribute("timezone", timeZone.toZoneId());
                    // Render HTML
                    return "page/event";
                })
                .orElse("page/event_not_found");
    }

    /**
     * Subscribe to an event by its ID
     *
     * @param id The event ID
     * @param referer The page originating the request
     * @param userDetails The logged user
     * @param session The user's session
     * @return The page to render
     */
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

    /**
     * Unsubscribe to an event by its ID
     *
     * @param id The event ID
     * @param referer The page originating the request
     * @param userDetails The logged user
     * @param session The user's session
     * @return The page to render
     */
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

    /**
     * Rate to an event by its ID
     *
     * @param id The event ID
     * @param referer The page originating the request
     * @param userDetails The logged user
     * @param session The user's session
     * @param dto The rating DTO
     * @return The page to render
     */
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

    /**
     * Approve to an event by its ID. Reserved to EVENT_MANAGER
     *
     * @param id The event ID
     * @param referer The page originating the request
     * @param session The user's session
     * @return The page to render
     */
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

    /**
     * Reject to an event by its ID. Reserved to EVENT_MANAGER
     *
     * @param id The event ID
     * @param referer The page originating the request
     * @param session The user's session
     * @return The page to render
     */
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

    /**
     * Unsubscribe a user to an event by its ID. Reserved to EVENT_MANAGER
     *
     * @param dto The user to remove
     * @param id The event ID
     * @param referer The page originating the request
     * @param session The user's session
     * @return The page to render
     */
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

    /**
     * Propose an event
     *
     * @param dto The DTO of the proposal
     * @param referer The page originating the request
     * @param session The user's session
     * @param timeZone The timezone of the client
     * @return The page to render
     */
    @PostMapping("/propose")
    public String postPropose(
            @ModelAttribute @Valid ProposeEventDto dto,
            @RequestHeader String referer,
            HttpSession session,
            TimeZone timeZone
    ) {
        log.info("postProposed called with dto: " + dto + " and timezone: " + timeZone);
        return eventService.proposeEvent(dtoMapper.toProposeEventDtoZoned(dto, timeZone))
                .map(eventId -> String.format("redirect:/event/%d", eventId))
                .orElseGet(() -> {
                    session.setAttribute("error", "propose_event_error");
                    return String.format("redirect:%s", referer);
                });
    }

}