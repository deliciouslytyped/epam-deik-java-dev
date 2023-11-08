package com.epam.training.ticketservice.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UserCommands {

    @ShellMethod(key = "describe account", value = "blah blah")
    public String describe() {
        return "";
    }
}
