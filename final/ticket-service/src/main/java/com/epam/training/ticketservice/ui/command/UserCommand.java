package com.epam.training.ticketservice.ui.command;


import com.epam.training.ticketservice.core.user.User;
import com.epam.training.ticketservice.core.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class UserCommand {

    private final UserService userService;

    @ShellMethod(key = "sign in privileged", value = "Sign in")
    public String signInPriviliged(String username, String password) {
        return userService.signInPriviliged(username,password)
                .map(user -> user + " is signed in!")
                .orElse("Wrong username or password!");
    }

    @ShellMethod(key = "sign up", value = "Sign up!")
    public String signUp(String username, String password){
        userService.signUp(username,password);
        return username + " account has been created!";
    }

    @ShellMethod(key = "sign in", value = "Signing in!")
    public String signIn(String username,String password){
        userService.signIn(username,password);
        return username + " has been signed in!";
    }

    @ShellMethod(key = "sign out", value = "Sign out")
    public String signOut() {

        return userService.signout()
                .map(user -> user + " is signed out!")
                .orElse("You need to login first!");
    }

    @ShellMethod(key = "describe account", value = "Get account information")
    public Optional<User> describe() {
        return userService.describe();
    }
}
