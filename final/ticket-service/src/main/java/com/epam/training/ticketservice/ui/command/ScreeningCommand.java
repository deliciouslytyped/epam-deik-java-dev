package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.user.persistence.Role;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {

    private final ScreeningService screeningService;
    private final UserService userService;
    private final MovieService movieService;
    private final RoomService roomService;


    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create a new screening.")
    public String createScreening(String movieName, String roomName, String date) { //TODO SZOBA LÉTEZIK-E
        return screeningService.createScreening(movieName, roomName, date);
    }




    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete a screening.")
    public String deleteScreening(String movieName, String roomName, String date) {
        screeningService.deleteScreening(movieName, roomName, date);
        return movieName + " " + roomName + " " + date + " screening has been deleted!";
    }

    @ShellMethod(key = "list screenings", value = "List the screenings.")
    public String listScreening() {
        if (screeningService.listScreening().isEmpty()) {
            return "There are no screenings at the moment";
        } else {
            return screeningService.listScreening().stream().map(screening ->
                    screening.getMovieName() + "("
                            + movieService.findMovie(screening.getMovieName()).get().getGenre()
                            + ", " + movieService.findMovie(screening.getMovieName()).get().getLength()
                            + " minutes), screened in room " + screening.getRoomName()
                            + ", at " + screening.getFormattedDate())
                    .collect(Collectors.joining("\n"));
        }
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().role() == Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
