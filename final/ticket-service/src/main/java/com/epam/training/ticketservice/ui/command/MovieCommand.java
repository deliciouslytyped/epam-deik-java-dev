package com.epam.training.ticketservice.ui.command;


import com.epam.training.ticketservice.core.movie.Movie;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.user.Role;
import com.epam.training.ticketservice.core.user.User;
import com.epam.training.ticketservice.core.user.UserDto;
import com.epam.training.ticketservice.core.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {

    private final MovieService movieService;
    private final UserService userService;
    //TODO conditions

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Create a new movie.")
    public String createMovie(String title, String genre, int length) {
        movieService.createMovie(title, genre, length);
        return title + " has been created!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Update an existing movie.")
    public String updateMovie(String title, String genre, int length) {
        movieService.updateMovie(title,genre,length);
        return title + "film has been updated!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Delete a movie.")
    public String deleteMovie(String title) {
        movieService.deleteMovie(title);
        return title + " has been deleted!";
    }

    @ShellMethod(key = "list movies", value = "List the movies.")
    public String listMovies() {
        if (movieService.listMovies().isEmpty()) {
            return "There are no movies at the moment!";
        }
        else return movieService.listMovies().stream().toString();
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().role() == Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }

}
