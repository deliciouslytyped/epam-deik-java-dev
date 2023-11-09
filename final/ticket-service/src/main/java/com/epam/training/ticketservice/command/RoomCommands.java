package com.epam.training.ticketservice.command;

import com.epam.training.ticketservice.service.RoomService;
import com.epam.training.ticketservice.util.OutputUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@RequiredArgsConstructor
public class RoomCommands extends PrivilegedCommands {
    private final RoomService service;

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "create room", value = "Usage: <name> <number of rows> <number of columns>")
    public String createRoom(String name, int rows, int columns) {
        var res = service.createRoom(name, rows, columns);
        return switch (res.state()) {
            case OK -> "Successfully created room";
            case ERROR -> "Failed to create room: " + res.error().getMessage();
        };
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "update room", value = "Usage: <name> <number of rows> <number of columns>")
    public String updateRoom(String name, int rows, int columns) {
        var res = service.updateRoom(name, rows, columns);
        return switch (res.state()) {
            case OK -> "Successfully updated room";
            case ERROR -> "Failed to update room: " + res.error().getMessage();
        };
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "delete room", value = "Usage: <name>")
    public String deleteRoom(String name) {
        var res = service.deleteRoom(name);
        return switch (res.state()) {
            case OK -> "Successfully deleted room";
            case ERROR -> "Failed to delete room: " + res.error().getMessage();
        };
    }

    @ShellMethod(key = "list rooms", value = "List all rooms.")
    public String listRooms() {
        var res = service.listRooms();
        if (res.isOk()) {
            return OutputUtils.toString(res.result(), "There are no rooms at the moment");
        } else {
            return "An error occurred: " + res.error().getMessage();
        }
    }
}
