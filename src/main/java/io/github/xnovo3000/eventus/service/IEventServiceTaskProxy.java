package io.github.xnovo3000.eventus.service;

import io.github.xnovo3000.eventus.api.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class IEventServiceTaskProxy {

    private final EventService eventService;

    @Scheduled(fixedRateString = "${io.github.xnovo3000.eventus.old_unapproved_event_removal_task_rate}")
    public void scheduledRemoveOldUnapprovedEvents() {
        log.info("Starting");
        if (eventService.deleteOldUnapprovedEvents()) {
            log.info("Removed old events");
        } else {
            log.error("Failed to remove old events");
        }
    }

}