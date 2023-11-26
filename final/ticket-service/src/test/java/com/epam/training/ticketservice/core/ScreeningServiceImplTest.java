package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.service.ScreeningServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ScreeningServiceImplTest {

    private static final Screening ENTITY = new Screening("It", "szoba", "2001-01-12 16:00");


    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final ScreeningServiceImpl underTest = new ScreeningServiceImpl(screeningRepository, roomRepository, movieRepository);

    private static Screening screening;
    private static Room room;
    private static Movie movie;
    private static String date;

    @BeforeEach
    void init() {
        movie = new Movie("movie", "genre", 30);
        room = new Room("room", 10, 10);
        date = "2000-01-01 12:00";
        screening = new Screening(movie.getTitle(), room.getName(), date);
        screeningRepository.save(screening);
        roomRepository.save(room);
        movieRepository.save(movie);
    }

    @AfterEach
    void del() {
        screeningRepository.delete(screening);
        movieRepository.deleteByTitle(movie.getTitle());
        roomRepository.deleteByName(room.getName());
    }

    @Test
    void testFindScreeningShouldReturnMovieNameRoomNameAndDateWhenInputScreeningIsEntity() {
        //Given
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("It", "szoba", LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))).thenReturn(Optional.of(ENTITY));
        Optional<Screening> expected = Optional.of(ENTITY);
        //When
        Optional<Screening> actual = underTest.findScreening("It", "szoba", "2001-01-01 10:00");

        //Then
        assertEquals(expected, actual);
        verify(screeningRepository).findByMovieNameAndRoomNameAndDate("It", "szoba", LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @Test
    void testFindScreeningShouldReturnOptionalEmptyWhenInputScreeningDoesNotExist() {
        // Given
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("dummy", "dummy", LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .thenReturn(Optional.empty());
        Optional<Screening> expected = Optional.empty();

        // When
        Optional<Screening> actual = underTest.findScreening("dummy", "dummy", "2001-01-01 10:00");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(screeningRepository).findByMovieNameAndRoomNameAndDate("dummy", "dummy", LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @Test
    void testFindScreeningShouldReturnOptionalEmptyWhenInputMovieNameAndRoomNameAreNull() {
        // Given
        when(screeningRepository.findByMovieNameAndRoomNameAndDate(null, null, LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .thenReturn(Optional.empty());
        Optional<Screening> expected = Optional.empty();

        // When
        Optional<Screening> actual = underTest.findScreening(null, null, "2001-01-01 10:00");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(screeningRepository).findByMovieNameAndRoomNameAndDate(null, null, LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
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
        when(screeningRepository.findByMovieNameAndRoomNameAndDate(ENTITY.getMovieName(), ENTITY.getRoomName(), ENTITY.getDate()))
                .thenReturn(Optional.of(ENTITY));
        when(screeningRepository.deleteByMovieNameAndRoomNameAndDate(ENTITY.getMovieName(), ENTITY.getRoomName(), ENTITY.getDate()))
                .thenReturn(1L);

        // When
        underTest.deleteScreening(ENTITY.getMovieName(), ENTITY.getRoomName(), ENTITY.getFormattedDate());

        // Then
        verify(screeningRepository).deleteByMovieNameAndRoomNameAndDate(ENTITY.getMovieName(), ENTITY.getRoomName(), ENTITY.getDate());
    }


    @Test
    void testIsOnBreakShouldReturnTrueIfOneOfTheScreeningsIsInTheOthersBreak() {
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("It","room",LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .thenReturn(Optional.of(new Screening("It","room","2001-01-01 10:00")));
        when(movieRepository.findByTitle("It"))
                .thenReturn(Optional.of(new Movie("It","horror",30)));
        when(roomRepository.findByName("room"))
                .thenReturn(Optional.of(new Room("room",10,10)));
        boolean result = underTest.isOnBreak(new Screening("It","room","2001-01-01 10:00"),
                new Screening("It","room","2001-01-01 10:35"));
        assertTrue(result);
    }

    @Test
    void testIsOnBreakShouldReturnFalseIfNoneOfTheScreeningsAreInEachOthersBreak() {
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("It","room",LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .thenReturn(Optional.of(new Screening("It","room","2001-01-01 10:00")));
        when(movieRepository.findByTitle("It"))
                .thenReturn(Optional.of(new Movie("It","horror",30)));
        when(roomRepository.findByName("room"))
                .thenReturn(Optional.of(new Room("room",10,10)));
        boolean result = underTest.isOnBreak(new Screening("It","room","2001-01-01 10:00"),
                new Screening("It","room","2001-01-01 11:00"));
        assertFalse(result);
    }

    @Test
    void testIsOverlapShouldReturnTrueIfOneOfTheScreeningsOverlapEachOther() {
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("It","room",LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .thenReturn(Optional.of(new Screening("It","room","2001-01-01 10:00")));
        when(movieRepository.findByTitle("It"))
                .thenReturn(Optional.of(new Movie("It","horror",30)));
        when(roomRepository.findByName("room"))
                .thenReturn(Optional.of(new Room("room",10,10)));
        boolean result = underTest.isOverlap(new Screening("It","room","2001-01-01 10:00"),
                new Screening("It","room","2001-01-01 10:10"));
        assertTrue(result);
    }

    @Test
    void testIsOverlapShouldReturnFalseIfOneOfTheScreeningsIsNotInTheOtherOnesBreak() {
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("It","room",LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .thenReturn(Optional.of(new Screening("It","room","2001-01-01 10:00")));
        when(movieRepository.findByTitle("It"))
                .thenReturn(Optional.of(new Movie("It","horror",30)));
        when(roomRepository.findByName("room"))
                .thenReturn(Optional.of(new Room("room",10,10)));

        boolean result = underTest.isOverlap(new Screening("It","room","2001-01-01 10:00"),
                new Screening("It","room","2001-01-01 11:00"));
        assertFalse(result);
    }

    @Test
    void testCreateScreeningShouldNotCreateTheScreeningIfTheInputScreeningsMovieIsInvalid() {
        when(movieRepository.findByTitle("NonExMovie")).thenReturn(Optional.empty());
        String result = underTest.createScreening("NonExMovie", "NonExistingRoom", "2023-01-01 12:00");

        assertEquals("Movie not exists", result);
    }

    @Test
    void testCreateScreeningShouldNotCreateTheScreeningIfTheInputScreeningsRoomIsInvalid() {
        when(movieRepository.findByTitle("NonExMovie")).thenReturn(Optional.of(new Movie("It","horror",30)));
        when(roomRepository.findByName("NonExistingRoom")).thenReturn(Optional.empty());
        String result = underTest.createScreening("NonExMovie", "NonExistingRoom", "2023-01-01 12:00");

        assertEquals("Room not exists", result);
    }

    @Test
    void testCreateScreeningShouldNotCreateTheScreeningIfTheInputScreeningOverLapsAnother() {
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("It","room",LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .thenReturn(Optional.of(new Screening("It","room","2001-01-01 10:00")));
        when(movieRepository.findByTitle("It"))
                .thenReturn(Optional.of(new Movie("It","horror",30)));
        when(roomRepository.findByName("room"))
                .thenReturn(Optional.of(new Room("room",10,10)));
        when(screeningRepository.findAll()).thenReturn(List.of(new Screening("It","room","2001-01-01 10:00")));
        String result = underTest.createScreening("It","room","2001-01-01 10:10");
        assertEquals("There is an overlapping screening",result);

    }

    @Test
    void testCreateScreeningShouldNotCreateTheScreeningIfTheInputScreeningIsInAnotherOnesBreak() {
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("It","room",LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .thenReturn(Optional.of(new Screening("It","room","2001-01-01 10:00")));
        when(movieRepository.findByTitle("It"))
                .thenReturn(Optional.of(new Movie("It","horror",30)));
        when(roomRepository.findByName("room"))
                .thenReturn(Optional.of(new Room("room",10,10)));
        when(screeningRepository.findAll()).thenReturn(List.of(new Screening("It","room","2001-01-01 10:00")));
        String result = underTest.createScreening("It","room","2001-01-01 10:35");
        assertEquals("This would start in the break period after another screening in this room",result);

    }

    @Test
    void testCreateScreeningShouldCreateTheScreeningIfTheInputScreeningIsValid() {
        when(screeningRepository.findByMovieNameAndRoomNameAndDate("It","room",LocalDateTime.parse("2001-01-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .thenReturn(Optional.of(new Screening("It","room","2001-01-01 10:00")));
        when(movieRepository.findByTitle("It"))
                .thenReturn(Optional.of(new Movie("It","horror",30)));
        when(roomRepository.findByName("room"))
                .thenReturn(Optional.of(new Room("room",10,10)));
        when(screeningRepository.findAll()).thenReturn(List.of(new Screening("It","room","2001-01-01 10:00")));
        String result = underTest.createScreening("It","room","2001-01-01 12:00");
        assertEquals("It room 2001-01-01 12:00 screening has been created!",result);

    }


}