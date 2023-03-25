package io.github.xnovo3000.eventus.bean.validation;

import java.util.Objects;

public class NotNullValidator<T> extends BeanValidator<T> {

    @Override
    public boolean validate(T value) {
        return Objects.nonNull(value) && validateNext(value);
    }

}