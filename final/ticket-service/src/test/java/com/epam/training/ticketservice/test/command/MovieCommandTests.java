package com.epam.training.ticketservice.test.command;

import com.epam.training.ticketservice.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@DirtiesContext
@ActiveProfiles("ci")
public class MovieCommandTests {
    @Autowired
    private Shell shell;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void testCreateMovie() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create movie Sátántangó drama 450");
        shell.evaluate(() -> "create movie Sátántangó drama 450");

        assertThat(movieRepository.findByTitle("Sátántangó")).isPresent();
    }

    @Test
    void testUpdateMovie() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create movie Sátántangó drama 450");
        shell.evaluate(() -> "update movie Sátántangó drama 500");
        shell.evaluate(() -> "update movie asd drama 500");

        var movie = movieRepository.findByTitle("Sátántangó").get();
        assertThat(movie.getLength()).isEqualTo(500);
    }

    @Test
    void testDeleteMovie() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create movie Sátántangó drama 450");
        shell.evaluate(() -> "delete movie Sátántangó");

        assertThat(movieRepository.findAll()).isEmpty();
    }

    @Test
    void testListMovies() {
        shell.evaluate(() -> "sign in privileged admin admin");
        shell.evaluate(() -> "create movie Sátántangó drama 450");
        shell.evaluate(() -> "create movie movie1 aminmation 125");
        shell.evaluate(() -> "create movie movie2 drama 154");
        shell.evaluate(() -> "list movies");

        assertThat(movieRepository.findAll()).hasSize(3);
    }
}
