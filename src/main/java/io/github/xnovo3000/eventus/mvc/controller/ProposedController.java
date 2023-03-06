package io.github.xnovo3000.eventus.mvc.controller;

import io.github.xnovo3000.eventus.bean.dto.input.ApproveRejectEventDto;
import io.github.xnovo3000.eventus.mvc.service.EventService;
import jakarta.servlet.http.HttpSession;
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
    public String get(
            Model model,
            @RequestAttribute(required = false) String error,
            @RequestParam(defaultValue = "1") @Min(1) Integer page
    ) {
        // Inject error
        model.addAttribute("error", error);
        // Set model
        model.addAttribute("proposed_event", eventService.getProposed(page));
        model.addAttribute("page", page);
        // Render HTML
        return "page/proposed";
    }

    @PostMapping("/approve")
    public String postApprove(
            @ModelAttribute @Valid ApproveRejectEventDto dto,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (!eventService.approveEvent(dto.getEventId())) {
            session.setAttribute("error", "approve_reject_error");
        }
        return String.format("redirect:%s", referer);
    }

    @PostMapping("/reject")
    public String postReject(
            @ModelAttribute @Valid ApproveRejectEventDto dto,
            @RequestHeader String referer,
            HttpSession session
    ) {
        if (!eventService.rejectEvent(dto.getEventId())) {
            session.setAttribute("error", "approve_reject_error");
        }
        return String.format("redirect:%s", referer);
    }

}