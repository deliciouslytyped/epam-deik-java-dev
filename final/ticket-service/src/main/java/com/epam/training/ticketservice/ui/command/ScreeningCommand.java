package com.epam.training.ticketservice.ui.command;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {


    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create a new screening.")
    public String createScreening(String movieName, String roomName, String date) {
        //if movie and room exists
        //not createable if there is an overlap screening
        return null;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete a screening.")
    public String deleteScreening(String movieName, String roomName, String date) {
        return null;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "list screenings", value = "List the screenings.")
    public String listScreening() {
        return null;
    }
}
