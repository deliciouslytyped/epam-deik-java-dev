package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.screening.Screening;
import com.epam.training.ticketservice.core.screening.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.ScreeningServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ScreeningServiceImplTest {

    private static final Screening ENTITY = new Screening("It","szoba","2001-01-12 16:00");

    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final ScreeningServiceImpl underTest = new ScreeningServiceImpl(screeningRepository);

    @Test
    void testFindScreeningShouldReturnMovieNameRoomNameAndDateWhenInputScreeningIsEntity() {
        //Given
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("It","szoba","2001-01-12 16:00")).thenReturn(Optional.of(ENTITY));
        Optional<Screening> expected = Optional.of(ENTITY);
        //When
        Optional<Screening> actual = underTest.findScreening("It","szoba","2001-01-12 16:00");

        //Then
        assertEquals(expected,actual);
        verify(screeningRepository).findByMovieNameAndRoomNameAndDate("It","szoba","2001-01-12 16:00");
    }

    @Test
    void testFindScreeningShouldReturnOptionalEmptyWhenInputScreeningDoesNotExist() {
        // Given
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("dummy","dummy","dummy")).thenReturn(Optional.empty());
        Optional<Screening> expected = Optional.empty();

        // When
        Optional<Screening> actual = underTest.findScreening("dummy","dummy","dummy");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(screeningRepository).findByMovieNameAndRoomNameAndDate("dummy","dummy","dummy");
    }

    @Test
    void testFindScreeningShouldReturnOptionalEmptyWhenInputScreeningArgumentsAreNull() {
        // Given
        when(screeningRepository.findByMovieNameAndRoomNameAndDate(null,null,null)).thenReturn(Optional.empty());
        Optional<Screening> expected = Optional.empty();

        // When
        Optional<Screening> actual = underTest.findScreening(null,null,null);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(screeningRepository).findByMovieNameAndRoomNameAndDate(null,null,null);
    }

    @Test
    void testCreateScreeningShouldStoreTheGivenScreeningWhenTheInputScreeningIsValid() {
        // Given
        when(screeningRepository.save(ENTITY)).thenReturn(ENTITY);

        // When
        underTest.createScreening(ENTITY.getMovieName(), ENTITY.getRoomName(), ENTITY.getFormattedDate());

        // Then
        verify(screeningRepository).save(ENTITY);
    }

    @Test
    void testListScreeningShouldReturnTheAvailableScreenings() {
        // Given
        when(screeningRepository.findAll()).thenReturn(List.of(ENTITY));

        // When
        List<Screening> actual = underTest.listScreening();

        // Then
        verify(screeningRepository).findAll();
        assertEquals(1, actual.size());
    }

    @Test
    void testDeleteScreeningShouldDeleteTheChosenScreening() {
        // Given
        when(screeningRepository.deleteByMovieNameAndRoomNameAndDate(ENTITY.getMovieName(), ENTITY.getRoomName(), ENTITY.getDate())).thenReturn(1L);

        // When
        underTest.deleteScreening(ENTITY.getMovieName(), ENTITY.getRoomName(), ENTITY.getFormattedDate());

        // Then
        verify(screeningRepository).deleteByMovieNameAndRoomNameAndDate(ENTITY.getMovieName(), ENTITY.getRoomName(), ENTITY.getDate());
    }
}
