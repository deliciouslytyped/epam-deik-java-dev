package com.epam.training.ticketservice.command;

import com.epam.training.ticketservice.service.ScreeningService;
import com.epam.training.ticketservice.util.OutputUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;

@ShellComponent
@RequiredArgsConstructor
public class ScreeningCommands extends PrivilegedCommands {
    private final ScreeningService service;

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "create screening", value = "Usage: <movie title> <room name> <start time>")
    public String createScreening(String movie, String room, String start) {
        var res = service.createScreening(movie, room, start);
        return switch (res.state()) {
            case OK -> "Successfully created screening";
            case ERROR -> "Failed to create screening: " + res.error().getMessage();
        };
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "delete screening", value = "Usage: <movie title> <room name> <start time>")
    public String deleteScreening(String movie, String room, String start) {
        var res = service.deleteScreening(movie, room, start);
        return switch (res.state()) {
            case OK -> "Successfully deleted screening";
            case ERROR -> "Failed to delete screening: " + res.error().getMessage();
        };
    }

    @ShellMethod(key = "list screenings", value = "List all screenings.")
    public String listScreenings() {
        var res = service.listScreenings();
        if (res.isOk()) {
            return OutputUtils.toString(res.result(), "There are no screenings");
        } else {
            return "An error occurred: " + res.error().getMessage();
        }
    }
}
