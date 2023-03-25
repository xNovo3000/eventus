package io.github.xnovo3000.eventus.bean.validation;

import io.github.xnovo3000.eventus.bean.entity.Event;

public class EventNotFullValidator extends BeanValidator<Event> {

    @Override
    public boolean validate(Event value) {
        return value.getSeats() == value.getHoldings().size() && validateNext(value);
    }

}