package com.epam.training.ticketservice.test.command;

import com.epam.training.ticketservice.component.BasePriceHolder;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repository.ComponentRepository;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("ci")
public class PricingCommandTests {
    @Autowired
    private Shell shell;
    @Autowired
    private BasePriceHolder priceHolder;
    @Autowired
    private ComponentRepository componentRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ScreeningRepository screeningRepository;

    @Test
    void testUpdateBasePrice() {
        int newPrice = 1000;

        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "update base price " + newPrice);

        assertThat(priceHolder.getBasePrice()).isEqualTo(newPrice);
    }

    @Test
    void testCreateComponent() {
        String componentName = "test";
        int componentPrice = 1000;

        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create price component " + componentName + " " + componentPrice);

        assertThat(componentRepository.findByName(componentName)).isPresent();
    }

    @Test
    void testAttachComponentToMovie() {
        String componentName = "test";
        int componentPrice = 1000;
        String movieTitle = "test";
        String movieGenre = "test";
        int movieLength = 100;

        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create price component " + componentName + " " + componentPrice);
        shell.evaluate(() -> "create movie " + movieTitle + " " + movieGenre + " " + movieLength);
        shell.evaluate(() -> "attach price component to movie " + componentName + " " + movieTitle);

        var movie = movieRepository.findByTitle(movieTitle);
        assertThat(movie).isNotEmpty();
        assertThat(movie.get().getPriceComponent()).isNotNull();
    }

    @Test
    void testAttachComponentToRoom() {
        String componentName = "test";
        int componentPrice = 1000;
        String roomName = "test";
        int roomRows = 10;
        int roomColumns = 10;

        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create price component " + componentName + " " + componentPrice);
        shell.evaluate(() -> "create room " + roomName + " " + roomRows + " " + roomColumns);
        shell.evaluate(() -> "attach price component to room " + componentName + " " + roomName);

        var room = roomRepository.findByName(roomName);
        assertThat(room).isNotEmpty();
        assertThat(room.get().getPriceComponent()).isNotNull();
    }

    @Disabled
    @Test
    void testAttachComponentToScreening() {
        String componentName = "test";
        int componentPrice = 1000;
        String movieTitle = "test";
        String movieGenre = "test";
        int movieLength = 100;
        String roomName = "test";
        int roomRows = 10;
        int roomColumns = 10;
        String screeningStart = "2021-04-01 10:00";

        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create price component " + componentName + " " + componentPrice);
        shell.evaluate(() -> "create movie " + movieTitle + " " + movieGenre + " " + movieLength);
        shell.evaluate(() -> "create room " + roomName + " " + roomRows + " " + roomColumns);
        shell.evaluate(() -> "create screening " + movieTitle + " " + roomName + " \"" + screeningStart + "\"");
        shell.evaluate(() -> "attach price component to screening " + componentName + " " + movieTitle + " " + roomName + " \"" + screeningStart + "\"");

        var screening = screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movieTitle, roomName, LocalDateTime.parse(screeningStart, Screening.TIME_FORMAT));
        assertThat(screening).isNotEmpty();
        assertThat(screening.get().getPriceComponent()).isNotNull();
    }
}
