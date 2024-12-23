package com.epam.training.ticketservice.presentation.cli;

import com.epam.training.ticketservice.lib.room.RoomCrudService;
import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;

@ShellComponent
@RequiredArgsConstructor
public class RoomCommands implements PrivilegedCommand {
    private final RoomCrudService service;

    @ShellMethod(key = "create room")
    public String create(String name, Integer rows, Integer columns) {
        try {
            service.create(new RoomDto(name, rows, columns));
            return "Successfully created room";
        } catch (ApplicationDomainException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "update room")
    public String update(String name, Integer rows, Integer columns) {
        try {
            service.update(new RoomDto(name, rows, columns));
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
        var rooms = service.list();
        if (rooms.isEmpty()) {
            return "There are no rooms at the moment";
        } else {
            return rooms.stream()
                    .map(r -> {
                        return FMT."Room \{r.getName()} with \{r.getColCount()*r.getRowCount()} seats, \{r.getRowCount()} rows and \{r.getColCount()} columns";
                    })
                    .collect(Collectors.joining("\n"));
        }
    }
}