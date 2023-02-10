package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.dto.EventBriefDto;
import io.github.xnovo3000.eventus.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    @Mapping(source = "creator.username", target = "creatorUsername")
    @Mapping(
        target = "holdingsUsername",
        expression = "java(event.getHoldings().stream().map(el -> el.getUser().getUsername()).toList())"
    )
    EventBriefDto toEventBriefDto(Event event);

}