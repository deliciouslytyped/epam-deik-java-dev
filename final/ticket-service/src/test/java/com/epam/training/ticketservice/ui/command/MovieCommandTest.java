package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.Movie;
import com.epam.training.ticketservice.core.movie.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.Shell;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("it")
public class MovieCommandTest {

    private static final Movie movie = new Movie("It","horror",100);

    @Autowired
    private Shell shell;

    @SpyBean
    private MovieService movieService;

    @Test
    void testMovieCreateCommandShouldNotSaveTheProductWhenUserIsNotAdmin() {
        //Given
        shell.evaluate(() -> "sign up root root");
        shell.evaluate(() -> "sign in root root");

        //When
        shell.evaluate(() -> "create movie It horror 100");

        //Then
        verify(movieService, times(0)).createMovie(movie);
    }

    @Test
    void testMovieCreateCommandShouldNotSaveTheMovieWhenNobodyIsLoggedIn() {
        //Given
        //When
        shell.evaluate(() -> "create movie It horror 100");

        //Then
        verify(movieService, times(0)).createMovie(movie);
    }

    @Test
    void testMovieCreateCommandShouldSaveTheMovieWhenAdminIsLoggedIn() {
        //Given
        shell.evaluate(() -> "sign in privileged admin admin");

        //When
        shell.evaluate(() -> "create movie It horror 100");

        //Then
        verify(movieService).createMovie(movie);
        assertTrue(movieService.listMovies().contains(movie));
    }
}
