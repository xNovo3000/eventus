package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import io.github.xnovo3000.eventus.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    /* Event */
    EventBriefDto toEventBriefDto(Event event);

}