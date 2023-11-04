package com.epam.training.ticketservice.ui.command;


import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class UserCommand {


    @ShellMethod(key = "sign in privileged", value = "Sign in")
    public String signIn(String username, String password){
        return username;
    }

    @ShellMethod(key = "sign out", value = "Sign out")
    public String signOut(){
        return null;
    }

    @ShellMethod(key = "describe account", value = "Get account information")
    public String describe(){
        return null;
    }

    @ShellMethod(key = "exit", value = "Exit.")
    public String exit(){
        return null;
    }

}
