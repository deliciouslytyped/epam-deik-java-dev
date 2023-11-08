package com.epam.training.ticketservice.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AuthCommands {

    @ShellMethod(key = "sign in privileged", value = "Sign in as an administrator account")
    public String loginAdmin(String username, String password) {
        return "";
    }

    @ShellMethod(key = "sign up", value = "Usage: <username> <password>")
    public String signup(String username, String password) {
        return "";
    }

    @ShellMethod(key = "sign out", value = "Sign out of the administrator account")
    public String logout() {
        return "";
    }

}
