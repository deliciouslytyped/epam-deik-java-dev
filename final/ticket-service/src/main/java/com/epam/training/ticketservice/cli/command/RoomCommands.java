package com.epam.training.ticketservice.cli.command;

import com.epam.training.ticketservice.lib.room.RoomService;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.lib.util.exceptions.ApplicationDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class RoomCommands {
    private final RoomService service;

    @ShellMethod(key = "create room")
    public String create(String name, Integer rows, Integer columns) {
        try {
            service.create(name, rows, columns);
            return "Successfully created room";
        } catch (ApplicationDomainException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "update room")
    public String update(String name, Integer rows, Integer columns) {
        try {
            service.update(name, rows, columns);
            return "Successfully updated room";
        } catch (ApplicationDomainException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "delete room")
    public String delete(String name) {
        service.delete(name);
        return "deleted room";
    }
    @ShellMethod(key = "list rooms")
    public String list() {
        return service.list().stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}