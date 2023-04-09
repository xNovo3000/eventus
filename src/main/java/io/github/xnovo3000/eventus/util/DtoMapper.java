package io.github.xnovo3000.eventus.util;

import io.github.xnovo3000.eventus.bean.dto.input.ProposeEventDto;
import io.github.xnovo3000.eventus.bean.dto.input.zoned.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.bean.dto.output.EventCardDto;
import io.github.xnovo3000.eventus.bean.dto.output.EventDto;
import io.github.xnovo3000.eventus.bean.dto.output.SubscriptionDto;
import io.github.xnovo3000.eventus.bean.dto.output.UserDto;
import io.github.xnovo3000.eventus.bean.entity.Authority;
import io.github.xnovo3000.eventus.bean.entity.Event;
import io.github.xnovo3000.eventus.bean.entity.Subscription;
import io.github.xnovo3000.eventus.bean.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;
import java.util.TimeZone;

/**
 * Map functions generated by MapStruct to map Entity to DTO beans
 */
@Mapper(componentModel = "spring", imports = {OffsetDateTime.class, Authority.class})
public interface DtoMapper {

    /* Event */

    /**
     * Create an EventDto starting from an Event. This method evaluates also the username
     * to check if 'canSubscribe', 'canUnsubscribe' and 'canRate' must be true or false
     *
     * @param event The Event
     * @param username The user's username
     * @return The EventDto
     */
    @Mapping(source = "event.creator.username", target = "creatorUsername")
    @Mapping(target = "canSubscribe", expression = "java(event.getStart().isAfter(OffsetDateTime.now()) && event.getHoldings().stream().noneMatch(it -> it.getUser().getUsername().equals(username)) && event.getSeats() > event.getHoldings().size() && event.getApproved())")
    @Mapping(target = "canUnsubscribe", expression = "java(event.getStart().isAfter(OffsetDateTime.now()) && event.getHoldings().stream().anyMatch(it -> it.getUser().getUsername().equals(username)))")
    @Mapping(target = "canRate", expression = "java(event.getEnd().isBefore(OffsetDateTime.now()) && event.getHoldings().stream().anyMatch(it -> it.getUser().getUsername().equals(username) && it.getRating() == null && it.getComment() == null))")
    EventDto toEventDto(Event event, String username);

    /**
     * Create an EventDto starting from an Event. The fields 'canSubscribe',
     * 'canUnsubscribe' and 'canRate' are set to false
     *
     * @param event The Event
     * @return The EventCardDto
     */
    @Mapping(target = "description", expression = "java(event.getDescription().substring(0, Math.min(event.getDescription().length(), 96)))")
    @Mapping(source = "creator.username", target = "creatorUsername")
    @Mapping(target = "occupiedSeats", expression = "java(event.getHoldings().size())")
    @Mapping(target = "canSubscribe", expression = "java(false)")
    @Mapping(target = "canUnsubscribe", expression = "java(false)")
    EventCardDto toEventCardDto(Event event);

    /**
     * Create an EventCardDto starting from an Event. This method evaluates also the username
     * to check if 'canSubscribe', 'canUnsubscribe' and 'canRate' must be true or false
     *
     * @param event The Event
     * @param username The user's username
     * @return The EventCardDto
     */
    @Mapping(target = "description", expression = "java(event.getDescription().substring(0, Math.min(event.getDescription().length(), 96)))")
    @Mapping(source = "event.creator.username", target = "creatorUsername")
    @Mapping(target = "occupiedSeats", expression = "java(event.getHoldings().size())")
    @Mapping(target = "canSubscribe", expression = "java(event.getHoldings().stream().noneMatch(it -> it.getUser().getUsername().equals(username)))")
    @Mapping(target = "canUnsubscribe", expression = "java(event.getHoldings().stream().anyMatch(it -> it.getUser().getUsername().equals(username)))")
    EventCardDto toEventCardDto(Event event, String username);

    /* Subscription */

    /**
     * Create a SubscriptionDto from a Subscription
     *
     * @param subscription The Subscription
     * @return The SubscriptionDto
     */
    @Mapping(source = "user.username", target = "username")
    SubscriptionDto toSubscriptionDto(Subscription subscription);

    /* ProposeEvent */

    /**
     * Create a ProposeEventDtoZoned from a ProposeEventDto and its timezone
     *
     * @param proposeEventDto The ProposeEventDtoZoned
     * @param timeZone The timezone of the user
     * @return The ProposeEventDtoZoned
     */
    @Mapping(target = "start", expression = "java(proposeEventDto.getStart().atZone(timeZone.toZoneId()).toOffsetDateTime())")
    @Mapping(target = "end", expression = "java(proposeEventDto.getEnd().atZone(timeZone.toZoneId()).toOffsetDateTime())")
    ProposeEventDtoZoned toProposeEventDtoZoned(ProposeEventDto proposeEventDto, TimeZone timeZone);

    /* User */

    /**
     * Create a UserDto from a User
     *
     * @param user The User
     * @return The UserDto
     */
    @Mapping(target = "authorities", expression = "java(user.getAuthorities().stream().map(Authority::getName).toList())")
    UserDto toUserDto(User user);

}