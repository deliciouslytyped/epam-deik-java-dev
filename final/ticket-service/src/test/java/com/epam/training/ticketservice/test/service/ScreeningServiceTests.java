package com.epam.training.ticketservice.test.service;

import com.epam.training.ticketservice.dto.ScreeningDto;
import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.impl.ScreeningServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScreeningServiceTests {
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @InjectMocks
    private ScreeningServiceImpl screeningService;

    private Movie movie;
    private Room room;
    private Screening screening;

    private String format(LocalDateTime time) {
        return Screening.TIME_FORMAT.format(time);
    }

    @BeforeEach
    void setup() {
        movie = new Movie("Fast and Furious", "action", 30);
        room = new Room("bedroom", 10, 10);
        screening = new Screening(movie, room, LocalDateTime.of(2023, 11, 26, 19, 0));
    }

    @Test
    void testScreeningListing() {
        when(screeningRepository.findAll()).thenReturn(List.of(screening));

        var result = screeningService.listScreenings();
        assertThat(result.isOk()).isTrue();
        assertThat(result.result()).containsExactly(new ScreeningDto(screening));
    }

    @Test
    void testScreeningCreation() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepository.existsByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(), screening.getStartTime())).thenReturn(false);

        var result = screeningService.createScreening(movie.getTitle(), room.getName(), format(screening.getStartTime()));
        assertThat(result.isOk()).isTrue();
        verify(screeningRepository).save(screening);
    }

    @Test
    void testScreeningCreationFailsWhenScreeningAlreadyExists() {
        when(screeningRepository.existsByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(), screening.getStartTime())).thenReturn(true);

        var result = screeningService.createScreening(movie.getTitle(), room.getName(), format(screening.getStartTime()));
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(AlreadyExistsException.class);
        verify(screeningRepository, never()).save(any());
    }

    @Test
    void testScreeningCreationFailsWhenMovieDoesNotExist() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.empty());

        var result = screeningService.createScreening(movie.getTitle(), room.getName(), format(screening.getStartTime()));
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(screeningRepository, never()).save(any());
    }

    @Test
    void testScreeningCreationFailsWhenRoomDoesNotExist() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.empty());

        var result = screeningService.createScreening(movie.getTitle(), room.getName(), format(screening.getStartTime()));
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(screeningRepository, never()).save(any());
    }

    @Test
    void testScreeningCreationFailsOnOverlap() {
        when(screeningRepository.existsByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(), screening.getStartTime())).thenReturn(false);
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepository.findAllByRoomName(room.getName())).thenReturn(List.of(
                new Screening(movie, room, LocalDateTime.of(2023, 11, 26, 18, 35)),
                new Screening(movie, room, LocalDateTime.of(2023, 11, 26, 18, 35))
        ));

        var result = screeningService.createScreening(movie.getTitle(), room.getName(), format(screening.getStartTime()));
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(OperationException.class);
        assertThat(result.error().getMessage()).isEqualTo("There is an overlapping screening");
        verify(screeningRepository, never()).save(any());
    }

    @Test
    void testScreeningCreationFailsOnBreakOverlap() {
        when(screeningRepository.existsByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(), screening.getStartTime())).thenReturn(false);
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepository.findAllByRoomName(room.getName())).thenReturn(List.of(new Screening(movie, room, LocalDateTime.of(2023, 11, 26, 18, 25))));

        var result = screeningService.createScreening(movie.getTitle(), room.getName(), format(screening.getStartTime()));
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(OperationException.class);
        assertThat(result.error().getMessage()).isEqualTo("This would start in the break period after another screening in this room");
        verify(screeningRepository, never()).save(any());
    }

    @Test
    void testScreeningDeletion() {
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(), screening.getStartTime())).thenReturn(Optional.of(screening));

        var result = screeningService.deleteScreening(movie.getTitle(), room.getName(), format(screening.getStartTime()));
        assertThat(result.isOk()).isTrue();
        verify(screeningRepository).delete(screening);
    }

    @Test
    void testScreeningDeletionFailsWhenScreeningDoesNotExist() {
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(), screening.getStartTime())).thenReturn(Optional.empty());

        var result = screeningService.deleteScreening(movie.getTitle(), room.getName(), format(screening.getStartTime()));
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(screeningRepository, never()).delete(any());
    }
}
