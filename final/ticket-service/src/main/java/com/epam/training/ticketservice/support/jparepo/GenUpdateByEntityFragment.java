package com.epam.training.ticketservice.support.jparepo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GenUpdateByEntityFragment {
    //String[] fields() default {};
    //Class entity();
}
