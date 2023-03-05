package io.github.xnovo3000.eventus.bean.dto.output;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private String creatorUsername;
    private OffsetDateTime creationDate;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private Boolean approved;
    private Integer seats;
    private List<SubscriptionDto> holdings;
    private Boolean canSubscribe;
    private Boolean canUnsubscribe;
    private Boolean canRate;
}