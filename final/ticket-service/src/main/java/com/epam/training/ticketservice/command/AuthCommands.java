package com.epam.training.ticketservice.command;

import com.epam.training.ticketservice.service.AuthenticationService;
import com.epam.training.ticketservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@RequiredArgsConstructor
public class AuthCommands extends PrivilegedCommands {
    private final AuthenticationService service;

    @ShellMethodAvailability("isSignedOut")
    @ShellMethod(key = "sign in", value = "Usage: <username> <password>")
    public String login(String username, String password) {
        var res = service.login(username, password, false);
        return switch (res.state()) {
            case OK -> "Sign in successful";
            case ERROR -> "Login failed due to incorrect credentials";
        };
    }

    @ShellMethodAvailability("isSignedOut")
    @ShellMethod(key = "sign in privileged", value = "Sign in as an administrator account")
    public String loginAdmin(String username, String password) {
        var res = service.login(username, password, true);
        return switch (res.state()) {
            case OK -> "Sign in successful";
            case ERROR -> "Login failed due to incorrect credentials";
        };
    }

    @ShellMethodAvailability("isSignedOut")
    @ShellMethod(key = "sign up", value = "Usage: <username> <password>")
    public String signup(String username, String password) {
        var res = service.signup(username, password);
        return switch (res.state()) {
            case OK -> "Signup successful";
            case ERROR -> "Failed to sign up: " + res.error().getMessage();
        };
    }

    @ShellMethodAvailability("isSignedIn")
    @ShellMethod(key = "sign out", value = "Sign out of the administrator account")
    public String logout() {
        var res = service.logout();
        return switch (res.state()) {
            case OK -> "Successfully signed out";
            case ERROR -> "Failed to sign out: " + res.error().getMessage();
        };
    }

}
