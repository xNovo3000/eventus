package io.github.xnovo3000.eventus.controller;

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
                .map(eventBriefDto -> "redirect:/event/" + eventBriefDto.getId())
                .orElse("redirect:/?propose_event_error");
    }

}