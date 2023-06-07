package io.github.xnovo3000.eventus.api.dto.output;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SubscriptionDto {
    private String username;
    private OffsetDateTime creationDate;
    private Integer rating;
    private String comment;
}