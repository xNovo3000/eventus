package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.bean.dto.*;
import io.github.xnovo3000.eventus.bean.entity.Event;
import io.github.xnovo3000.eventus.bean.entity.Participation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.TimeZone;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    /* Event */

    @Mapping(source = "creator.username", target = "creatorUsername")
    EventDto toEventDto(Event event);

    @Mapping(target = "description", expression = "java(event.getDescription().substring(0, Math.min(event.getDescription().length(), 96)))")
    @Mapping(source = "creator.username", target = "creatorUsername")
    @Mapping(target = "occupiedSeats", expression = "java(event.getHoldings().size())")
    @Mapping(target = "canSubscribe", expression = "java(false)")
    @Mapping(target = "canUnsubscribe", expression = "java(false)")
    EventCardDto toEventCardDto(Event event);

    @Mapping(target = "description", expression = "java(event.getDescription().substring(0, Math.min(event.getDescription().length(), 96)))")
    @Mapping(source = "event.creator.username", target = "creatorUsername")
    @Mapping(target = "occupiedSeats", expression = "java(event.getHoldings().size())")
    @Mapping(target = "canSubscribe", expression = "java(event.getHoldings().stream().noneMatch(it -> it.getUser().getUsername().equals(username)))")
    @Mapping(target = "canUnsubscribe", expression = "java(event.getHoldings().stream().anyMatch(it -> it.getUser().getUsername().equals(username)))")
    EventCardDto toEventCardDto(Event event, String username);

    /* ProposeEvent */

    @Mapping(target = "start", expression = "java(proposeEventDto.getStart().atZone(timeZone.toZoneId()).toOffsetDateTime())")
    @Mapping(target = "end", expression = "java(proposeEventDto.getEnd().atZone(timeZone.toZoneId()).toOffsetDateTime())")
    ProposeEventDtoZoned toProposeEventDtoZoned(ProposeEventDto proposeEventDto, TimeZone timeZone);

}