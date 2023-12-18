package com.epam.training.ticketservice.cli.command;

import com.epam.training.ticketservice.lib.movie.MovieCrudServiceImpl;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class MovieCommands {
    private final MovieCrudServiceImpl service;

    @ShellMethod(key = "create movie")
    public String create(String title, String genre, int runtime) {
        try {
            service.create(new MovieDto(title, genre, runtime));
            return "Successfully created movie";
        } catch (ApplicationDomainException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "update movie")
    public String update(String title, String genre, int runtime) {
        try {
            service.update(new MovieDto(title, genre, runtime));
            return "Successfully updated movie";
        } catch (ApplicationDomainException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "delete movie")
    public String delete(String title) {
        service.delete(title);
        return "deleted movie";
    }
    @ShellMethod(key = "list movies")
    public String list() {
        return service.list().stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}