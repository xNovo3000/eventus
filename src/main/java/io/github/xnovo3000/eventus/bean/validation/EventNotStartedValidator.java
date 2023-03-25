package io.github.xnovo3000.eventus.bean.validation;

import io.github.xnovo3000.eventus.bean.entity.Event;

import java.time.OffsetDateTime;

public class EventNotStartedValidator extends BeanValidator<Event> {

    @Override
    public boolean validate(Event value) {
        return value.getStart().isBefore(OffsetDateTime.now()) && validateNext(value);
    }

}