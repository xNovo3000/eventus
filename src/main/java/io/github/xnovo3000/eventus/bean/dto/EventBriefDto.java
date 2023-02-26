package io.github.xnovo3000.eventus.bean.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class EventBriefDto {
    private Long id;
    private String name;
    private String description;
    private String creatorUsername;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private Integer seats;
    private List<String> holdingsUsername;
}