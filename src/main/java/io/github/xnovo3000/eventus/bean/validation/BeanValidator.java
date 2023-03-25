package io.github.xnovo3000.eventus.bean.validation;

import lombok.val;

public abstract class BeanValidator<T> {

    private BeanValidator<T> next;

    public abstract boolean validate(T value);

    protected boolean validateNext(T value) {
        if (next == null) {
            return true;
        }
        return next.validate(value);
    }

    @SafeVarargs
    public static <S> BeanValidator<S> create(BeanValidator<S> head, BeanValidator<S>... rest) {
        BeanValidator<S> current = head;
        for (val validator : rest) {
            current.next = validator;
            current = validator;
        }
        return head;
    }

}