package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.Room;
import com.epam.training.ticketservice.core.room.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class RoomCommand {
    //TODO conditions

    private final RoomService roomService;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Create a new room.")
    public String createRoom(String name, int rows, int cols) {
        roomService.createRoom(name, rows, cols);
        return name + " room has been created!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Update a room.")
    public String updateRoom(String name, int rows, int cols) {
        roomService.updateRoom(name, rows, cols);
        return name + " room has been updated!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Delete a room.")
    public String deleteRoom(String name) {
        roomService.deleteRoom(name);
        return name + " room has been deleted!";
    }

    @ShellMethod(key = "list rooms", value = "List the rooms.")
    public List<Room> listRoom() {
        return roomService.listRoom();
    }

}
