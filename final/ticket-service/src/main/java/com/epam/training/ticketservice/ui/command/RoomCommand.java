package com.epam.training.ticketservice.ui.command;

import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@AllArgsConstructor
public class RoomCommand {

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Create a new room.")
    public String createRoom(String name, int rows, int columns){
        return null;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Update a room.")
    public String updateRoom(String name, int rows, int columns){
        return null;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Delete a room.")
    public String deleteRoom(String name){
        return null;
    }

    @ShellMethod(key = "list rooms", value = "List the rooms.")
    public String listRoom(){
        return null;
    }




    private Availability isAvailable(){
        return isAdmin()
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }

    private boolean isAdmin() {
        return true;
    }
}
