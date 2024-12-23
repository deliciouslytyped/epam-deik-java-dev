package com.epam.training.ticketservice.presentation.cli;

import com.epam.training.ticketservice.lib.movie.MovieCrudServiceImpl;
import com.epam.training.ticketservice.lib.screening.ScreeningCrudService;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;

import static com.epam.training.ticketservice.presentation.cli.Util.parseTime;
import static java.util.FormatProcessor.FMT;

@ShellComponent
@RequiredArgsConstructor
public class ScreeningCommands implements PrivilegedCommand {
    private final ScreeningCrudService service;
    private final MovieCrudServiceImpl ms;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault());

    @ShellMethod(key = "create screening")
    public String create(String title, String room, String date) {
        try {
            service.create(new ScreeningDto(null, title, room, parseTime(date)));
            return "Successfully created screening";
        } catch (ApplicationDomainException e) {
            if (e.getMessage().equals("You can't create a screening that overlaps with the 10 minute break after another screening.")){
                return "This would start in the break period after another screening in this room";
            } else if (e.getMessage().equals("You can't create a screening that overlaps with another screening in the same room.")) { //TODO yeah this is a terrible way to do this
                return "There is an overlapping screening";
            }
            else {
                return e.getMessage();
            }
        }
    }
    @ShellMethod(key = "list screenings")
    public String list() {
        var screenings = service.list();
        if (screenings.isEmpty()) {
            return "There are no screenings";
        } else {
            return screenings.stream()
                    .sorted(Comparator.comparing(ScreeningDto::getId))
                    .map(s -> {
                        var movie = ms.getByTitle(s.getMovieTitle()).get();
                        return FMT."\{s.getMovieTitle()} (\{movie.getGenre()}, \{movie.getRuntime()} minutes), screened in room \{s.getRoomName()}, at \{formatter.format(s.getTime())}";
                    })
                    .collect(Collectors.joining("\n"));
        }
    }

    @ShellMethod(key = "delete screening")
    public String deleteScreening(String title, String roomName, String time) {
        var sdto = new ScreeningDto(null, title, roomName, Util.parseTime(time));
        service.deleteByAlternateKey(service.getMapper().dtoToAlternateKey(sdto));
        return "";
    }
}