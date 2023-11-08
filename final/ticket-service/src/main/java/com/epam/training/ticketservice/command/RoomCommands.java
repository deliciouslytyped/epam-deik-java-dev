package com.epam.training.ticketservice.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class RoomCommands {

    @ShellMethod(key = "create room", value = "Usage: <name> <number of rows> <number of columns>")
    public String createRoom(String name, int rows, int columns) {
        return "";
    }

    @ShellMethod(key = "update room", value = "Usage: <name> <number of rows> <number of columns>")
    public String updateRoom(String name, int rows, int columns) {
        return "";
    }

    @ShellMethod(key = "delete room", value = "Usage: <name>")
    public String deleteRoom(String name) {
        return "";
    }

    @ShellMethod(key = "list rooms", value = "List all rooms.")
    public String listRooms() {
        return "";
    }
}
