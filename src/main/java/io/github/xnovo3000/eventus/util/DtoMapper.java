package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.bean.dto.EventBriefDto;
import io.github.xnovo3000.eventus.bean.dto.EventDto;
import io.github.xnovo3000.eventus.bean.dto.EventParticipationDto;
import io.github.xnovo3000.eventus.bean.entity.Event;
import io.github.xnovo3000.eventus.bean.entity.Participation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    /* Event */

    @Mapping(source = "creator.username", target = "creatorUsername")
    @Mapping(
        target = "holdingsUsername",
        expression = "java(event.getHoldings().stream().map(el -> el.getUser().getUsername()).toList())"
    )
    EventBriefDto toEventBriefDto(Event event);

    @Mapping(source = "creator.username", target = "creatorUsername")
    EventDto toEventDto(Event event);

    /* Participation */

    @Mapping(source = "user.username", target = "username")
    EventParticipationDto toEventParticipationDto(Participation participation);

}