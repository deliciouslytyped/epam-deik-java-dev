package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.core.movie.service.MovieServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MovieServiceImplTest {

    private static final Movie ENTITY = new Movie("It","horror",120);

    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final MovieServiceImpl underTest = new MovieServiceImpl(movieRepository);

    @Test
    void testFindMovieShouldReturnMovieWhenInputProductNameIsValidAndStored() {
        //Given
        when(movieRepository.findByTitle("It")).thenReturn(Optional.of(ENTITY));
        Optional<MovieDto> expected = Optional.of(new MovieDto(ENTITY.getTitle(),ENTITY.getGenre(),ENTITY.getLength()));
        //When
        Optional<MovieDto> actual = underTest.findMovie("It");

        //Then
        assertEquals(expected,actual);
        verify(movieRepository).findByTitle("It");
    }

    @Test
    void testFindMovieShouldReturnOptionalEmptyWhenInputMovieNameDoesNotExist() {
        // Given
        when(movieRepository.findByTitle("dummy")).thenReturn(Optional.empty());
        Optional<MovieDto> expected = Optional.empty();

        // When
        Optional<MovieDto> actual = underTest.findMovie("dummy");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(movieRepository).findByTitle("dummy");
    }

    @Test
    void testFindMovieShouldReturnOptionalEmptyWhenInputMovieNameIsNull() {
        // Given
        when(movieRepository.findByTitle(null)).thenReturn(Optional.empty());
        Optional<MovieDto> expected = Optional.empty();

        // When
        Optional<MovieDto> actual = underTest.findMovie(null);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(movieRepository).findByTitle(null);
    }

    @Test
    void testCreateMovieShouldStoreTheGivenMovieWhenTheInputMovieIsValid() {
        // Given
        when(movieRepository.save(ENTITY)).thenReturn(ENTITY);

        // When
        underTest.createMovie(ENTITY);

        // Then
        verify(movieRepository).save(ENTITY);
    }

    @Test
    void testListMoviesShouldReturnTheAvailableMovies() {
        // Given
        when(movieRepository.findAll()).thenReturn(List.of(ENTITY));

        // When
        List<MovieDto> actual = underTest.listMovies();

        // Then
        verify(movieRepository).findAll();
        assertEquals(1, actual.size());
    }

    @Test
    void testDeleteMovieShouldDeleteTheChosenMovie() {
        // Given
        when(movieRepository.deleteByTitle(ENTITY.getTitle())).thenReturn(1L);

        // When
        underTest.deleteMovie(ENTITY.getTitle());

        // Then
        verify(movieRepository).deleteByTitle(ENTITY.getTitle());
    }


    @Test
    void testUpdateRoomShouldUpdateTheRoom() {
        //Given
        when(movieRepository.findByTitle(ENTITY.getTitle())).thenReturn(Optional.of(ENTITY));

        //When
        underTest.updateMovie(ENTITY.getTitle(), "krimi", 123);
        //Then
        assertEquals(ENTITY,new Movie("It","krimi",123));
    }

}
