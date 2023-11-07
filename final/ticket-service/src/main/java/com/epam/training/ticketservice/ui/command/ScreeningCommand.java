package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieRepository;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.room.RoomRepository;
import com.epam.training.ticketservice.core.room.RoomService;
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
    public String createScreening(String movieName, String roomName, String date) {
        Screening createScreening = new Screening(movieName,roomName,date);

        List<Screening> overlaps = new ArrayList<>();
        List<Screening> breaks = new ArrayList<>();
        if (movieService.findMovie(movieName).isPresent() && roomService.findRoom(roomName).isPresent())
            screeningService.listScreening() //TODO ha üres az kérdéses hogy lefut-e !!!!!!!!!!!!!!!!!!!!!!! ezt ahol kinlódok a új sorba minden listát
                    .forEach(screening -> {
                        LocalDateTime screeningStart = screening.getDate();
                        LocalDateTime screeningEnd = screening.getDate().plusMinutes(movieService.findMovie(screening.getMovieName()).get().getLength());
                        LocalDateTime createScreeningStart = createScreening.getDate();
                        LocalDateTime createScreeningEnd = createScreeningStart.plusMinutes(movieService.findMovie(screening.getMovieName()).get().getLength());
                        if (screening.getRoomName().equals(roomName) &&
                                !((createScreeningStart.isAfter(screeningStart) && (createScreeningStart.isAfter(screeningEnd) || createScreeningStart.isEqual(screeningEnd))) ||
                                ((createScreeningEnd.isBefore(screeningStart) || createScreeningEnd.isEqual(screeningStart)) && createScreeningEnd.isBefore(screeningEnd))))
                            overlaps.add(screening);
                        if (screening.getRoomName().equals(roomName) &&
                                !((createScreeningStart.isAfter(screeningStart) && (createScreeningStart.isAfter(screeningEnd.plusMinutes(10)) || createScreeningStart.isEqual(screeningEnd.plusMinutes(10)))) ||
                                        ((createScreeningEnd.plusMinutes(10).isBefore(screeningStart) || createScreeningEnd.plusMinutes(10).isEqual(screeningStart)) && createScreeningEnd.isBefore(screeningEnd))))
                            breaks.add(screening);
                    });
        if (!overlaps.isEmpty()) return "There is an overlapping screening";
        if (!breaks.isEmpty()) return "This would start on the break period after another screening in this room";
        screeningService.createScreening(movieName, roomName, date);
        return movieName + " " + roomName + " " + date + " screening has been created!";
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
            System.out.println("There are no screenings at the moment");
        } else {
            String result = screeningService.listScreening().stream().map(screening ->
                    screening.getMovieName() + "(" +
                    movieService.findMovie(screening.getMovieName()).get().getGenre() +
                    ", " + movieService.findMovie(screening.getMovieName()).get().getLength() +
                    " minutes), screened in room " + screening.getRoomName() + ", at " + screening.getDate())
                    .collect(Collectors.joining("\n"));

            return result;
        }
        return null;
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().role() == Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
