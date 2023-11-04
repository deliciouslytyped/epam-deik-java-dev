package com.epam.training.ticketservice.ui.command;


import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Create a new movie.")
    public String createMovie(String title, String genre, int length){
        return null;
    }

    @ShellMethod(key = "update movie", value = "Update an existing movie.")
    public String updateMovie(String title, String genre, int length){
        return null;
    }

    @ShellMethod(key = "delete movie", value = "Delete a movie.")
    public String deleteMovie(String title){
        return null;
    }

    @ShellMethod(key = "list movies", value = "List the movies.")
    public String listMovies(){
        return null;
    }

    private Availability isAvailable(){
        return isAdmin()
                ? Availability.available()
                : Availability.unavailable("You are not an admin!")
    }

    private boolean isAdmin() {
        return true;
    }

}
