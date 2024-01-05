package com.epam.training.ticketservice.lib.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html#meta-annotations
//TODO can I do something so that instead of having to bloody annotate everything I can default deny a subset of methods and permit where i want?
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority(T(com.epam.training.ticketservice.lib.security.Roles).ROLE_ADMIN)")
public @interface IsAuthedAdmin { }
