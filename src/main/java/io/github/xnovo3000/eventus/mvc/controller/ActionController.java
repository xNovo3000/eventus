package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.bean.dto.input.ApproveRejectEventDto;
import io.github.xnovo3000.eventus.bean.dto.input.ParticipateToEventDto;
import io.github.xnovo3000.eventus.bean.dto.input.ProposeEventDto;
import io.github.xnovo3000.eventus.bean.dto.input.zoned.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.security.JpaUserDetails;
import io.github.xnovo3000.eventus.mvc.service.EventService;
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

    @PostMapping("/approve_event")
    public String approveEvent(@ModelAttribute @Valid ApproveRejectEventDto approveEventDto) {
        if (eventService.approveOrRejectEvent(approveEventDto.getEventId(), true)) {
            return "redirect:/event/%d".formatted(approveEventDto.getEventId());
        } else {
            return "redirect:/propose_event?approve_event_error";
        }
    }

    @PostMapping("/reject_event")
    public String rejectEvent(@ModelAttribute @Valid ApproveRejectEventDto rejectEventDto) {
        if (eventService.approveOrRejectEvent(rejectEventDto.getEventId(), false)) {
            return "redirect:/propose_event?reject_event_success";
        } else {
            return "redirect:/propose_event?reject_event_error";
        }
    }

}