package com.epam.training.ticketservice.test.command;

import com.epam.training.ticketservice.component.BasePriceHolder;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repository.ComponentRepository;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.test.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DirtiesContext
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

    @BeforeEach
    void setup() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create room Pedersoli 20 10");
        shell.evaluate(() -> "create room Girotti 10 10");
        shell.evaluate(() -> "create movie Sátántangó drama 450");
        shell.evaluate(() -> "create movie movie1 aminmation 125");
        shell.evaluate(() -> "create movie movie2 drama 154");
        shell.evaluate((In)() -> "create screening Sátántangó Pedersoli \"2021-03-15 10:45\"");
        shell.evaluate((In)() -> "create screening movie1 Pedersoli \"2021-03-14 16:00\"");
        shell.evaluate((In)() -> "create screening movie2 Girotti \"2021-03-14 16:00\"");
        shell.evaluate(() -> "sign out");
    }

    @Test
    void testUpdateBasePrice() {
        int newPrice = 1000;

        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "update base price " + newPrice);

        assertThat(priceHolder.getBasePrice()).isEqualTo(newPrice);
    }

    @Test
    void testAssignPriceComponentToMovie() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create price component additionalFeeForSatantango 100");
        shell.evaluate(() -> "attach price component to movie additionalFeeForSatantango Sátántangó");
        shell.evaluate(() -> "attach price component to movie nemletezik Sátántangó");
        shell.evaluate((In)() -> "show price for Sátántangó Pedersoli \"2021-03-15 10:45\" 10,5");

        var movie = movieRepository.findByTitle("Sátántangó").get();
        assertThat(movie.getPriceComponent()).isNotNull();
        assertThat(movie.getPriceComponent().getName()).isEqualTo("additionalFeeForSatantango");
        assertThat(movie.getPriceComponent().getAmount()).isEqualTo(100);
    }

    @Test
    void testAssignPriceComponentToRoom() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create price component additionalFeeForPedersoli 100");
        shell.evaluate(() -> "attach price component to room additionalFeeForPedersoli Pedersoli");
        shell.evaluate(() -> "attach price component to room nemletezik Pedersoli");
        shell.evaluate((In)() -> "show price for movie2 Girotti \"2021-03-14 16:00\" 5,5");

        var room = roomRepository.findByName("Pedersoli").get();
        assertThat(room.getPriceComponent()).isNotNull();
        assertThat(room.getPriceComponent().getName()).isEqualTo("additionalFeeForPedersoli");
        assertThat(room.getPriceComponent().getAmount()).isEqualTo(100);
    }

    @Test
    void testAssignPriceComponentToScreening() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create price component additionalFeeForPulpFictionScreening 100");
        shell.evaluate((In)() -> "attach price component to screening additionalFeeForPulpFictionScreening movie2 Girotti \"2021-03-14 16:00\"");
        shell.evaluate((In)() -> "attach price component to screening nemletezik movie2 Girotti \"2021-03-14 16:00\"");

        var time = LocalDateTime.parse("2021-03-14 16:00", Screening.TIME_FORMAT);
        var screening = screeningRepository.findByMovieTitleAndRoomNameAndStartTime("movie2", "Girotti", time).get();
        assertThat(screening.getPriceComponent()).isNotNull();
        assertThat(screening.getPriceComponent().getName()).isEqualTo("additionalFeeForPulpFictionScreening");
        assertThat(screening.getPriceComponent().getAmount()).isEqualTo(100);
    }
}
