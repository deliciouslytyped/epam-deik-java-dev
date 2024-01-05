package com.epam.training.ticketservice.lib.security.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultPrivilegedBase {
    String value();

    String[] checkedMethods() default {}; // currently unused because annotations dont have inheritance
}