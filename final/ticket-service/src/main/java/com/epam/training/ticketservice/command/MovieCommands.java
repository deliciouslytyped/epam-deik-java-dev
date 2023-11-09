package com.epam.training.ticketservice.command;

import com.epam.training.ticketservice.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;

@ShellComponent
@RequiredArgsConstructor
public class MovieCommands {
    private final MovieService service;

    @ShellMethod(key = "create movie", value = "Usage: <title> <category> <length in minutes>")
    public String createMovie(String title, String category, int length) {
        var res = service.createMovie(title, category, length);
        return switch (res.state()) {
            case OK -> "Successfully created movie";
            case ERROR -> "Failed to create movie: " + res.error().getMessage();
        };
    }

    @ShellMethod(key = "update movie", value = "Usage: <title> <category> <length in minutes>")
    public String updateMovie(String title, String category, int length) {
        var res = service.updateMovie(title, category, length);
        return switch (res.state()) {
            case OK -> "Successfully updated movie";
            case ERROR -> "Failed to update movie: " + res.error().getMessage();
        };
    }

    @ShellMethod(key = "delete movie", value = "Usage: <title>")
    public String deleteMovie(String title) {
        var res = service.deleteMovie(title);
        return switch (res.state()) {
            case OK -> "Successfully deleted movie";
            case ERROR -> "Failed to delete movie: " + res.error().getMessage();
        };
    }

    @ShellMethod(key = "list movies", value = "List all movies")
    public String listMovies() {
        var res = service.listMovies();
        if (res.isOk()) {
            var sb = new StringBuilder();
            if (res.result().isEmpty()) {
                sb.append("There are no movies at the moment");
            } else {
                res.result().forEach(m -> sb.append(m).append("\n"));
            }
            sb.setLength(sb.length() - "\n".length());
            return sb.toString();
        } else {
            return "An error occurred: " + res.error().getMessage();
        }
    }
}
