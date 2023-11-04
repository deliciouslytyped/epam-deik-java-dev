package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.screening.Screening;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Date;
import java.util.List;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {

    private final ScreeningService screeningService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create a new screening.")
    public String createScreening(String movieName, String roomName, Date date) {
        screeningService.createScreening(movieName, roomName, date);
        return movieName + " " + roomName + " " + date + " screening has been created!";
        //if movie and room exists
        //not createable if there is an overlap screening

    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete a screening.")
    public String deleteScreening(String movieName, String roomName, Date date) {
        screeningService.deleteScreening(movieName, roomName, date);
        return movieName + " " + roomName + " " + date + " screening has been updated!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "list screenings", value = "List the screenings.")
    public List<Screening> listScreening() {
        return screeningService.listScreening();
    }
}
