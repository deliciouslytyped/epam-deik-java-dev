package com.epam.training.ticketservice.ui.command;


import com.epam.training.ticketservice.core.movie.Movie;
import com.epam.training.ticketservice.core.movie.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {

    private final MovieService movieService;
    //TODO conditions

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Create a new movie.")
    public String createMovie(String title, String genre, int length) {
        movieService.createMovie(title, genre, length);
        return title + " has been created!";
    }

    @ShellMethod(key = "update movie", value = "Update an existing movie.")
    public String updateMovie(String title, String genre, int length) {
        movieService.updateMovie(title,genre,length);
        return title + "film has been updated!";
    }

    @ShellMethod(key = "delete movie", value = "Delete a movie.")
    public String deleteMovie(String title) {
        movieService.deleteMovie(title);
        return title + " has been deleted!";
    }

    @ShellMethod(key = "list movies", value = "List the movies.")
    public List<Movie> listMovies() {
        return movieService.listMovies();
    }

    private Availability isAvailable() {
        return isAdmin()
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }

    private boolean isAdmin() {
        return true;
    }

}
