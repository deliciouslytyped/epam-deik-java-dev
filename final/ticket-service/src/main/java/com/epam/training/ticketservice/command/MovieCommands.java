package com.epam.training.ticketservice.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class MovieCommands {

    @ShellMethod(key = "create movie", value = "Usage: <title> <category> <length in minutes>")
    public String createMovie(String title, String category, int length) {
        return "";
    }

    @ShellMethod(key = "update movie", value = "Usage: <title> <category> <length in minutes>")
    public String updateMovie(String title, String category, int length) {
        return "";
    }

    @ShellMethod(key = "delete movie", value = "Usage: <title>")
    public String deleteMovie(String title) {
        return "";
    }

    @ShellMethod(key = "list movies", value = "List all movies")
    public String listMovies() {
        return "";
    }
}
