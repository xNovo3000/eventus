package io.github.xnovo3000.eventus.bean.dto.input.zoned;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ProposeEventDtoZoned {
    private String name;
    private String description;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private Integer seats;
}