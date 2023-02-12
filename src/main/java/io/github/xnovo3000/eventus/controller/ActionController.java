package io.github.xnovo3000.eventus.controller;

import io.github.xnovo3000.eventus.dto.ParticipateToEventDto;
import io.github.xnovo3000.eventus.dto.ProposeEventDto;
import io.github.xnovo3000.eventus.dto.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import io.github.xnovo3000.eventus.service.EventService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.TimeZone;

@Controller
@RequestMapping("/action")
@Validated
@AllArgsConstructor
public class ActionController {

    private final EventService eventService;

    @PostMapping("/propose_event")
    public String proposeEvent(
            @AuthenticationPrincipal JpaUserDetails userDetails,
            @ModelAttribute @Valid ProposeEventDto proposeEventDto,
            TimeZone timeZone
    ) {
        // Create zoned DTO
        ProposeEventDtoZoned proposeEventDtoZoned = new ProposeEventDtoZoned();
        proposeEventDtoZoned.setName(proposeEventDto.getName());
        proposeEventDtoZoned.setDescription(proposeEventDto.getDescription());
        proposeEventDtoZoned.setStart(proposeEventDto.getStart().atZone(timeZone.toZoneId()).toOffsetDateTime());
        proposeEventDtoZoned.setEnd(proposeEventDto.getEnd().atZone(timeZone.toZoneId()).toOffsetDateTime());
        proposeEventDtoZoned.setSeats(proposeEventDto.getSeats());
        // Try to create event. Success: go to the event page. Error: return to home with error
        return eventService.proposeEvent(proposeEventDtoZoned, userDetails.getUsername())
                .map("redirect:/event/%d"::formatted)
                .orElse("redirect:/?propose_event_error");
    }

    @PostMapping("/participate")
    public String participate(
            @AuthenticationPrincipal JpaUserDetails userDetails,
            @ModelAttribute @Valid ParticipateToEventDto participateToEventDto
    ) {
        if (eventService.setParticipationToEvent(participateToEventDto.getEventId(), userDetails.getUsername(), true)) {
            return "redirect:/event/%d".formatted(participateToEventDto.getEventId());
        } else {
            return "redirect:/event/%d?participate_error".formatted(participateToEventDto.getEventId());
        }
    }

    @PostMapping("/dont_participate")
    public String dontParticipate(
            @AuthenticationPrincipal JpaUserDetails userDetails,
            @ModelAttribute @Valid ParticipateToEventDto participateToEventDto
    ) {
        if (eventService.setParticipationToEvent(participateToEventDto.getEventId(), userDetails.getUsername(), false)) {
            return "redirect:/event/%d".formatted(participateToEventDto.getEventId());
        } else {
            return "redirect:/event/%d?dont_participate_error".formatted(participateToEventDto.getEventId());
        }
    }

}