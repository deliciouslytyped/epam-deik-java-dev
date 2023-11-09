package com.epam.training.ticketservice.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class UserCommands extends PrivilegedCommands {

    @ShellMethodAvailability("isSignedIn")
    @ShellMethod(key = "describe account", value = "blah blah")
    public String describe() {
        return "";
    }
}
