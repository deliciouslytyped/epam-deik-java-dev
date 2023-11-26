package com.epam.training.ticketservice.test.command;

import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.test.In;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@DirtiesContext
@ActiveProfiles("ci")
public class BookingCommandTests {
    @Autowired
    private Shell shell;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void setup() {
        shell.evaluate(() -> "sign up sanyi asdQWE123");
        shell.evaluate(() -> "sign up laci asdQWE123");
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create room Pedersoli 20 10");
        shell.evaluate(() -> "create room Girotti 10 10");
        shell.evaluate(() -> "create movie Sátántangó drama 450");
        shell.evaluate((In)() -> "create movie \"Spirited Away\" animation 125");
        shell.evaluate((In)() -> "create screening \"Spirited Away\" Girotti \"2021-03-15 10:00\"");
        shell.evaluate((In)() -> "create screening Sátántangó Pedersoli \"2021-03-15 10:45\"");
    }

    @Test
    void testBooking() {
        shell.evaluate(() -> "sign in sanyi asdQWE123");
        shell.evaluate((In)() -> "book Sátántangó Pedersoli \"2021-03-15 10:45\" \"5,5 5,6\"");
        shell.evaluate((In)() -> "book Sátántangó Pedersoli \"2021-03-15 10:45\" \"5,5 5,6\"");
        shell.evaluate((In)() -> "describe account");

        assertThat(bookingRepository.findAll()).hasSize(1);
    }
}
