package com.epam.training.ticketservice.ui.command;


import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.service.MovieService;
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
public class MovieCommand {

    private final MovieService movieService;
    private final UserService userService;
    //TODO conditions

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "create movie", value = "Create a new movie.")
    public String createMovie(String title, String genre, int length) {
        movieService.createMovie(new Movie(title,genre,length));
        return title + " movie has been created!";
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "update movie", value = "Update an existing movie.")
    public String updateMovie(String title, String genre, int length) {
        movieService.updateMovie(title,genre,length);
        return title + " movie has been updated!";
    }

    @ShellMethodAvailability("isAdmin")
    @ShellMethod(key = "delete movie", value = "Delete a movie.")
    public String deleteMovie(String title) {
        movieService.deleteMovie(title);
        return title + " movie has been deleted!";
    }

    @ShellMethod(key = "list movies", value = "List the movies.")
    public String listMovies() {
        if (movieService.listMovies().isEmpty()) {
            return "There are no movies at the moment";
        } else {
            return movieService.listMovies().stream().map(movie ->
                            movie.title() + " (" + movie.genre()
                            + ", " + movie.length() + " minutes)")
                    .collect(Collectors.joining("\n"));
        }
    }

    private Availability isAdmin() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().role() == Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }

}
