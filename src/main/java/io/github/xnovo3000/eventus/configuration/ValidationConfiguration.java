package io.github.xnovo3000.eventus.configuration;

import io.github.xnovo3000.eventus.bean.dto.input.zoned.ProposeEventDtoZoned;
import io.github.xnovo3000.eventus.bean.entity.Event;
import io.github.xnovo3000.eventus.bean.entity.User;
import io.github.xnovo3000.eventus.bean.validation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfiguration {

    @Bean
    public BeanValidator<ProposeEventDtoZoned> proposeEventBeanValidator() {
        return BeanValidator.create(
                new NotNullValidator<>(),
                new ProposeEventZonedDateValidator()
        );
    }

    @Bean
    public BeanValidator<Event> approveEventBeanValidator() {
        return BeanValidator.create(
                new NotNullValidator<>(),
                new EventApprovedValidator(false),
                new EventNotStartedValidator()
        );
    }

    @Bean
    public BeanValidator<Event> rejectEventBeanValidator() {
        return BeanValidator.create(
                new NotNullValidator<>(),
                new EventApprovedValidator(false)
        );
    }

    @Bean
    public BeanValidator<Event> subscribeUserToEventValidator() {
        return BeanValidator.create(
                new NotNullValidator<>(),
                new EventApprovedValidator(true),
                new EventNotStartedValidator(),
                new EventNotFullValidator()
        );
    }

    @Bean
    public BeanValidator<Event> unsubscribeUserToEventValidator() {
        return BeanValidator.create(
                new NotNullValidator<>(),
                new EventApprovedValidator(true),
                new EventNotStartedValidator()
        );
    }

    @Bean
    public BeanValidator<User> userServiceValidator() {
        return BeanValidator.create(
                new NotNullValidator<>(),
                new UserNotAdminValidator()
        );
    }

}