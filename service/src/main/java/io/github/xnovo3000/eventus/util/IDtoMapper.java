package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.api.dto.input.ProposeEventDto;
import io.github.xnovo3000.eventus.api.dto.input.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.api.dto.output.EventCardDto;
import io.github.xnovo3000.eventus.api.dto.output.EventDto;
import io.github.xnovo3000.eventus.api.dto.output.SubscriptionDto;
import io.github.xnovo3000.eventus.api.dto.output.UserDto;
import io.github.xnovo3000.eventus.api.entity.Authority;
import io.github.xnovo3000.eventus.api.entity.Event;
import io.github.xnovo3000.eventus.api.entity.Subscription;
import io.github.xnovo3000.eventus.api.entity.User;
import io.github.xnovo3000.eventus.api.util.DtoMapper;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

/**
 * Default implementation for DtoMapper
 */
@Component
public class IDtoMapper implements DtoMapper {

    @Override
    public @NotNull EventDto toEventDto(@NotNull Event event, String username) {
        val eventDto = new EventDto();
        eventDto.setCreatorUsername(event.getCreator().getUsername());
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setDescription(event.getDescription());
        eventDto.setCreationDate(event.getCreationDate());
        eventDto.setStart(event.getStart());
        eventDto.setEnd(event.getEnd());
        eventDto.setApproved(event.getApproved());
        eventDto.setSeats(event.getSeats());
        eventDto.setHoldings(event.getHoldings().stream().map(this::toSubscriptionDto).toList());
        return eventDto;
    }

    @Override
    public @NotNull EventCardDto toEventCardDto(@NotNull Event event) {
        return toEventCardDto(event, null);
    }

    @Override
    public @NotNull EventCardDto toEventCardDto(@NotNull Event event, String username) {
        val eventCardDto = new EventCardDto();
        eventCardDto.setCreatorUsername(event.getCreator().getUsername());
        eventCardDto.setId(event.getId());
        eventCardDto.setName(event.getName());
        eventCardDto.setCreationDate(event.getCreationDate());
        eventCardDto.setStart(event.getStart());
        eventCardDto.setEnd(event.getEnd());
        eventCardDto.setApproved(event.getApproved());
        eventCardDto.setSeats(event.getSeats());
        eventCardDto.setDescription(event.getDescription().substring(0, Math.min(event.getDescription().length(), 96)));
        eventCardDto.setOccupiedSeats(event.getHoldings().size());
        eventCardDto.setCanSubscribe(event.getHoldings().stream().noneMatch(it -> it.getUser().getUsername().equals(username)));
        eventCardDto.setCanUnsubscribe(event.getHoldings().stream().anyMatch(it -> it.getUser().getUsername().equals(username)));
        return eventCardDto;
    }

    @Override
    public @NotNull SubscriptionDto toSubscriptionDto(@NotNull Subscription subscription) {
        val subscriptionDto = new SubscriptionDto();
        subscriptionDto.setUsername(subscription.getUser().getUsername());
        subscriptionDto.setCreationDate(subscription.getCreationDate());
        subscriptionDto.setRating(subscription.getRating());
        subscriptionDto.setComment(subscription.getComment());
        return subscriptionDto;
    }

    @Override
    public @NotNull ProposeEventDtoZoned toProposeEventDtoZoned(
            @NotNull ProposeEventDto proposeEventDto,
            @NotNull TimeZone timeZone
    ) {
        val proposeEventDtoZoned = new ProposeEventDtoZoned();
        proposeEventDtoZoned.setName(proposeEventDto.getName());
        proposeEventDtoZoned.setDescription(proposeEventDto.getDescription());
        proposeEventDtoZoned.setSeats(proposeEventDto.getSeats());
        proposeEventDtoZoned.setStart(proposeEventDto.getStart().atZone(timeZone.toZoneId()).toOffsetDateTime());
        proposeEventDtoZoned.setEnd(proposeEventDto.getEnd().atZone(timeZone.toZoneId()).toOffsetDateTime());
        return proposeEventDtoZoned;
    }

    @Override
    public @NotNull UserDto toUserDto(@NotNull User user) {
        val userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setActive(user.getActive());
        userDto.setAuthorities(user.getAuthorities().stream().map(Authority::getName).toList());
        return userDto;
    }

}