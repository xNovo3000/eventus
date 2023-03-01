package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.bean.dto.ApproveRejectEventDto;
import io.github.xnovo3000.eventus.mvc.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proposed")
@Validated
@AllArgsConstructor
public class ProposedController {

    private final EventService eventService;

    @GetMapping
    public String get(Model model, @RequestParam(defaultValue = "1") @Min(1) Integer page) {
        // Set model
        model.addAttribute("proposed_event", eventService.getProposed(page));
        model.addAttribute("page", page);
        // Render HTML
        return "page/proposed";
    }

    @PostMapping("/approve")
    public String postApprove(@ModelAttribute @Valid ApproveRejectEventDto dto, @RequestHeader String referer) {
        if (eventService.approveEvent(dto.getEventId())) {
            return String.format("redirect:%s", referer);
        } else {
            return String.format("redirect:%s?approve_reject_error", referer);
        }
    }

    @PostMapping("/reject")
    public String postReject(@ModelAttribute @Valid ApproveRejectEventDto dto, @RequestHeader String referer) {
        if (eventService.rejectEvent(dto.getEventId())) {
            return String.format("redirect:%s", referer);
        } else {
            return String.format("redirect:%s?approve_reject_error", referer);
        }
    }

}