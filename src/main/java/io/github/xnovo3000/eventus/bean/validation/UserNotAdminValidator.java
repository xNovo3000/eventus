package io.github.xnovo3000.eventus.bean.validation;

import io.github.xnovo3000.eventus.bean.entity.User;

import java.util.Objects;

public class UserNotAdminValidator extends BeanValidator<User> {

    @Override
    public boolean validate(User value) {
        return !Objects.equals(value.getUsername(), "admin") && validateNext(value);
    }

}