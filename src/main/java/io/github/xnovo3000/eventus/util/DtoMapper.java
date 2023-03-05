package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.bean.dto.input.ProposeEventDto;
import io.github.xnovo3000.eventus.bean.dto.input.zoned.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.bean.dto.output.EventCardDto;
import io.github.xnovo3000.eventus.bean.dto.output.EventDto;
import io.github.xnovo3000.eventus.bean.dto.output.SubscriptionDto;
import io.github.xnovo3000.eventus.bean.entity.Event;
import io.github.xnovo3000.eventus.bean.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;
import java.util.TimeZone;

@Mapper(componentModel = "spring", imports = OffsetDateTime.class)
public interface DtoMapper {

    /* Event */

    @Mapping(source = "creator.username", target = "creatorUsername")
    @Mapping(target = "canSubscribe", expression = "java(false)")
    @Mapping(target = "canUnsubscribe", expression = "java(false)")
    @Mapping(target = "canRate", expression = "java(false)")
    EventDto toEventDto(Event event);

    @Mapping(source = "event.creator.username", target = "creatorUsername")
    @Mapping(target = "canSubscribe", expression = "java(event.getHoldings().stream().noneMatch(it -> it.getUser().getUsername().equals(username)) && event.getSeats() > event.getHoldings().size() && event.getApproved())")
    @Mapping(target = "canUnsubscribe", expression = "java(event.getHoldings().stream().anyMatch(it -> it.getUser().getUsername().equals(username)))")
    @Mapping(target = "canRate", expression = "java(event.getEnd().isBefore(OffsetDateTime.now()) && event.getHoldings().stream().anyMatch(it -> it.getUser().getUsername().equals(username) && it.getRating() != null && it.getComment() != null))")
    EventDto toEventDto(Event event, String username);

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

    /* Participation */

    @Mapping(source = "user.username", target = "username")
    SubscriptionDto toSubscriptionDto(Subscription subscription);

    /* ProposeEvent */

    @Mapping(target = "start", expression = "java(proposeEventDto.getStart().atZone(timeZone.toZoneId()).toOffsetDateTime())")
    @Mapping(target = "end", expression = "java(proposeEventDto.getEnd().atZone(timeZone.toZoneId()).toOffsetDateTime())")
    ProposeEventDtoZoned toProposeEventDtoZoned(ProposeEventDto proposeEventDto, TimeZone timeZone);

}