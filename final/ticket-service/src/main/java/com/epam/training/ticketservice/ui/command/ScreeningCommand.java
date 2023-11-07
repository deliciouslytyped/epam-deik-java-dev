package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieRepository;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.room.RoomRepository;
import com.epam.training.ticketservice.core.screening.Screening;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.user.Role;
import com.epam.training.ticketservice.core.user.UserDto;
import com.epam.training.ticketservice.core.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {

    private final ScreeningService screeningService;
    private final UserService userService;
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final RoomRepository roomRepository;

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create a new screening.")
    public String createScreening(String movieName, String roomName, String date) {
        Screening createScreening = new Screening(movieName,roomName,date);

        List<Screening> overlaps = new ArrayList<>();
        if (movieRepository.findByTitle(movieName).isPresent() && roomRepository.findByName(roomName).isPresent())
            listScreening() //TODO ha üres az kérdéses hogy lefut-e
                    .forEach(screening -> {
                        LocalDateTime screeningStart = screening.getDate();
                        LocalDateTime screeningEnd = screening.getDate().plusMinutes(movieRepository.findByTitle(screening.getMovieName()).get().getLength() + 10);
                        LocalDateTime createScreeningStart = createScreening.getDate();
                        LocalDateTime createScreeningEnd = createScreeningStart.plusMinutes(movieRepository.findByTitle(screening.getMovieName()).get().getLength() + 10);
                        if (screening.getRoomName().equals(roomName) &&
                                !((createScreeningStart.isAfter(screeningStart) && (createScreeningStart.isAfter(screeningEnd) || createScreeningStart.isEqual(screeningEnd))) ||
                                ((createScreeningEnd.isBefore(screeningStart) || createScreeningEnd.isEqual(screeningStart)) && createScreeningEnd.isBefore(screeningEnd))))
                            overlaps.add(screening);
                    });
        if (!overlaps.isEmpty()) return "There is an overlapping screening";
        screeningService.createScreening(movieName, roomName, date);
        return movieName + " " + roomName + " " + date + " screening has been created!";
        //if movie and room exists
        //not createable if there is an overlap screening

    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete a screening.")
    public String deleteScreening(String movieName, String roomName, String date) {
        screeningService.deleteScreening(movieName, roomName, date);
        return movieName + " " + roomName + " " + date + " screening has been deleted!";
    }

    @ShellMethod(key = "list screenings", value = "List the screenings.")
    public List<Screening> listScreening() {
        if (screeningService.listScreening().isEmpty()) {
            System.out.println("There are no screenings at the moment");
        }
        else return screeningService.listScreening();
        return null;
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().role() == Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
