package io.github.xnovo3000.eventus.bean.validation;

import io.github.xnovo3000.eventus.bean.entity.Event;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventApprovedValidator extends BeanValidator<Event> {

    private final boolean approved;

    @Override
    public boolean validate(Event value) {
        return (value.getApproved() == approved) && validateNext(value);
    }

}