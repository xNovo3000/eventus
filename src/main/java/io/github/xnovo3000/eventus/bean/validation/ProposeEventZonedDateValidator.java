package io.github.xnovo3000.eventus.bean.validation;

import io.github.xnovo3000.eventus.bean.dto.input.zoned.ProposeEventDtoZoned;

public class ProposeEventZonedDateValidator extends BeanValidator<ProposeEventDtoZoned> {

    @Override
    public boolean validate(ProposeEventDtoZoned value) {
        return value.getStart().isBefore(value.getEnd()) && validateNext(value);
    }

}