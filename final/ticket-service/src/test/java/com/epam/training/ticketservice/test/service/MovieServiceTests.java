package com.epam.training.ticketservice.test.service;

import com.epam.training.ticketservice.dto.MovieDto;
import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTests {
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie movie;

    @BeforeEach
    void setup() {
        movie = new Movie("Toy Story 3", "animation", 350);
    }

    @Test
    void testMovieListing() {
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        var result = movieService.listMovies();
        assertThat(result.isOk()).isTrue();
        assertThat(result.result()).containsExactly(new MovieDto(movie));
    }

    @Test
    void testMovieCreation() {
        when(movieRepository.existsByTitleIgnoreCase(movie.getTitle())).thenReturn(false);

        var result = movieService.createMovie(movie.getTitle(), movie.getCategory(), movie.getLength());
        assertThat(result.isOk()).isTrue();
        verify(movieRepository).save(movie);
    }

    @Test
    void testMovieCreationFailsWhenMovieAlreadyExists() {
        when(movieRepository.existsByTitleIgnoreCase(movie.getTitle())).thenReturn(true);

        var result = movieService.createMovie(movie.getTitle(), movie.getCategory(), movie.getLength());
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void testMovieDeletion() {
        when(movieRepository.existsByTitleIgnoreCase(movie.getTitle())).thenReturn(true);

        var result = movieService.deleteMovie(movie.getTitle());
        assertThat(result.isOk()).isTrue();
        verify(movieRepository).deleteByTitle(movie.getTitle());
    }

    @Test
    void testMovieDeletionFailsWhenMovieDoesNotExist() {
        when(movieRepository.existsByTitleIgnoreCase(movie.getTitle())).thenReturn(false);

        var result = movieService.deleteMovie(movie.getTitle());
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
    }

    @Test
    void testMovieUpdate() {
        var movie = spy(this.movie);
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(java.util.Optional.of(movie));

        var result = movieService.updateMovie(movie.getTitle(), movie.getCategory(), movie.getLength());
        assertThat(result.isOk()).isTrue();
        verify(movieRepository).save(movie);
        verify(movie).setCategory(any());
        verify(movie).setLength(anyInt());
    }

    @Test
    void testMovieUpdateFailsWhenMovieDoesNotExist() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(java.util.Optional.empty());

        var result = movieService.updateMovie(movie.getTitle(), movie.getCategory(), movie.getLength());
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
    }
}
