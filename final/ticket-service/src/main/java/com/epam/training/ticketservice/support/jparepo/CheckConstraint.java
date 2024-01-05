package com.epam.training.ticketservice.support.jparepo;

import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import org.intellij.lang.annotations.Language;

import java.lang.annotation.Repeatable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

@Repeatable(CheckConstraints.class)
public @interface CheckConstraint {
    String driverFilter() default "";

    String name();

    //Make intellij have a little highlighting
    @Language(value = "SQL", prefix = "ALTER TABLE table ADD CONSTRAINT name CHECK (", suffix = ")")
    String check();

    Class[] alsoDependsOn() default {};
}
