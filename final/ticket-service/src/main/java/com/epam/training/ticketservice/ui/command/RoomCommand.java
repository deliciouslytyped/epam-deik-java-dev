package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.user.persistence.Role;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class RoomCommand {
    //TODO conditions

    private final RoomService roomService;
    private final UserService userService;

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
    public String listRoom() {
        if (roomService.listRoom().isEmpty()) {
            System.out.println("There are no rooms at the moment");
        } else {
            return roomService.listRoom().stream().map(room ->
                    "Room " + room.getName()
                            + " with " + room.getCols() * room.getRows()
                            + " seats, " + room.getRows() + " rows and "
                            + room.getCols() + " columns"
                    )
                    .collect(Collectors.joining("\n"));
        }
        return null;
    }


    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().role() == Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
