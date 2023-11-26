package com.epam.training.ticketservice.ui.command;


import com.epam.training.ticketservice.core.user.persistence.Role;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class UserCommand {

    private final UserService userService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "sign in privileged", value = "Sign in for admins.")
    public String signInPrivileged(String username, String password) {
        return userService.signInPrivileged(username,password)
                .map(userDto -> userDto.username() + " is signed in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign up", value = "Sign up.")
    public String signUp(String username, String password) {
        userService.signUp(username,password);
        return username + " account has been created!";
    }

    @ShellMethod(key = "sign in", value = "Sign in for users.")
    public String signIn(String username,String password) {
        return userService.signIn(username,password)
                .map(userDto -> userDto.username() + " is signed in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign out", value = "Sign out.")
    public String signOut() {
        return userService.signout()
                .map(userDto -> userDto.username() + " is signed out!")
                .orElse("You need to login first!");
    }

    @ShellMethod(key = "describe account", value = "Get account information.")
    public String describe() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent()) {
            if (user.get().role() == Role.ADMIN) {
                return "Signed in with privileged account "
                        + "'" + user.get().username() + "'";
            } else {
                return "Signed in with account "
                        + "'" + user.get().username() + "'";
            }
        } else {
            return "You are not signed in";
        }
    }
}
