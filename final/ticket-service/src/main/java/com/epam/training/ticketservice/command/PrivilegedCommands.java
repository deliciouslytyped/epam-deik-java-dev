package com.epam.training.ticketservice.command;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.shell.Availability;

public abstract class PrivilegedCommands {

    protected Availability isAdmin() {
        var auth = getAuth();
        if (!(auth instanceof UsernamePasswordAuthenticationToken)) {
            return Availability.unavailable("You are not signed in");
        }
        if (auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("ROLE_ADMIN"))) {
            return Availability.unavailable("L bozo");
        }

        return Availability.available();
    }

    protected Availability isSignedIn() {
        var auth = getAuth();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            return Availability.available();
        }
        return Availability.unavailable("You are not signed in");
    }

    protected Availability isSignedOut() {
        var auth = getAuth();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            return Availability.unavailable("already signed in");
        }
        return Availability.available();
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
