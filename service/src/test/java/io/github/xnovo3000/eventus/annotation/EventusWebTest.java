package io.github.xnovo3000.eventus.annotation;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@EventusTest
@AutoConfigureMockMvc
public @interface EventusWebTest {}