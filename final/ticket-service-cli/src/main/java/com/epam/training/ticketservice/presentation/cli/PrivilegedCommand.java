package com.epam.training.ticketservice.presentation.cli;

import com.epam.training.ticketservice.lib.security.Roles;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
interface PrivilegedCommand {

    @ShellMethodAvailability({
            "create movie", "update movie", "delete movie",
            "create room", "update room", "delete room",
            "create screening", "update screening", "delete screening",
            "update base price", "create price component", "attach price component to room", "attach price component to movie", "attach price component to screening"
    })
    default Availability privilegedAvailabilityCheck() {
        var auth = getAuth();
        return (auth != null && auth.isAuthenticated() && isPrivileged(auth))
                ? Availability.available()
                : Availability.unavailable("You are not logged in privileged");
    }

    @ShellMethodAvailability({"book"})
    default Availability loggedInAvailibilityCheck() {
        var auth = getAuth();
        return (auth != null && auth.isAuthenticated() && getRole(auth).equals(Roles.ROLE_USER.toString()))
                ? Availability.available()
                : Availability.unavailable("You are not logged in as a normal user");
    }

    public static Authentication getAuth() {
        var auth = SecurityContextHolder.getContext().getAuthentication(); //TODO may be null if unauthenticated
        return auth;
    }

    static boolean isPrivileged(Authentication auth) {
        var role = getRole(auth);
        var isPrivileged = role.equals(Roles.ROLE_ADMIN.toString());
        return isPrivileged;
    }

    @NonNull
    static String getRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .get();
    }

    public static String getUsername(Authentication auth) {
        String uname;
        uname = ((User) auth.getPrincipal()).getUsername();
        return uname;
    }


}
