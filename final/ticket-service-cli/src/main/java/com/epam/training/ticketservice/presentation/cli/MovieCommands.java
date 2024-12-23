package com.epam.training.ticketservice.presentation.cli;

import com.epam.training.ticketservice.lib.movie.MovieCrudServiceImpl;
import com.epam.training.ticketservice.lib.movie.model.MovieDto;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;

@ShellComponent
@RequiredArgsConstructor
public class MovieCommands implements PrivilegedCommand {
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
        var rooms = service.list();
        if (rooms.isEmpty()) {
            return "There are no movies at the moment";
        } else {
            return rooms.stream()
                    .map(m -> {
                        return FMT."\{m.getTitle()} (\{m.getGenre()}, \{m.getRuntime()} minutes)";
                    })
                    .collect(Collectors.joining("\n"));
        }
    }
}