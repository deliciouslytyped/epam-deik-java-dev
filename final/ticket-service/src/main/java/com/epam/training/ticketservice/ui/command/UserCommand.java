package com.epam.training.ticketservice.ui.command;


import com.epam.training.ticketservice.core.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class UserCommand {

    private final UserService userService;

    @ShellMethod(key = "sign in privileged", value = "Sign in")
    public String signIn(String username, String password){
        return userService.signin(username,password)
                .map(User -> User + " is signed in!")
                .orElse("Wrong username or password!");
    }

    @ShellMethod(key = "sign out", value = "Sign out")
    public String signOut(){

        return userService.signout()
                .map(User -> User + " is signed out!")
                .orElse("You need to login first!");
    }

    @ShellMethod(key = "describe account", value = "Get account information")
    public String describe(){
        //return userService.describe();
        return null;
    }
}
