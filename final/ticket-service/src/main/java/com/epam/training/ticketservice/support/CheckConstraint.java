package com.epam.training.ticketservice.support;

import java.lang.annotation.Repeatable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

@Repeatable(CheckConstraints.class)
public @interface CheckConstraint {
    String name();
    String check();
}
