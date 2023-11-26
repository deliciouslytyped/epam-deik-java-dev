package com.epam.training.ticketservice.test.service;

import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.repository.ComponentRepository;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.service.impl.ComponentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComponentServiceTests {
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private ComponentRepository componentRepository;

    @InjectMocks
    private ComponentServiceImpl componentService;

    private Movie movie;
    private Room room;
    private Screening screening;
    private PriceComponent component;

    private String format(LocalDateTime time) {
        return Screening.TIME_FORMAT.format(time);
    }

    @BeforeEach
    void setUp() {
        movie = new Movie("test", "test", 100);
        room = new Room("test", 10, 10);
        screening = new Screening(movie, room, LocalDateTime.of(2023, 11, 11, 11, 0));
        component = new PriceComponent("test", 100);
    }

    @Test
    void testComponentCreation() {
        when(componentRepository.existsByName(component.getName())).thenReturn(false);

        var result = componentService.createComponent(component.getName(), component.getAmount());
        assertThat(result.isOk()).isTrue();
        verify(componentRepository).save(component);
    }

    @Test
    void testComponentCreationFailsWhenComponentAlreadyExists() {
        when(componentRepository.existsByName(component.getName())).thenReturn(true);

        var result = componentService.createComponent(component.getName(), component.getAmount());
        assertThat(result.isErr()).isTrue();
        assertThat(result.error()).isInstanceOf(AlreadyExistsException.class);
        verify(componentRepository, never()).save(component);
    }

    @Test
    void testAttachComponentToMovie() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(componentRepository.findByName(component.getName())).thenReturn(Optional.of(component));

        var result = componentService.attachToMovie(component.getName(), movie.getTitle());
        assertThat(result.isOk()).isTrue();
        assertThat(movie.getPriceComponent()).isEqualTo(component);
        verify(movieRepository).save(movie);
    }

    @Test
    void testAttachComponentToMovieFailsWhenMovieDoesNotExist() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.empty());

        var result = componentService.attachToMovie(component.getName(), movie.getTitle());
        assertThat(result.isErr()).isTrue();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(movieRepository, never()).save(movie);
    }

    @Test
    void testAttachComponentToMovieFailsWhenComponentDoesNotExist() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(componentRepository.findByName(component.getName())).thenReturn(Optional.empty());

        var result = componentService.attachToMovie(component.getName(), movie.getTitle());
        assertThat(result.isErr()).isTrue();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(movieRepository, never()).save(movie);
    }

    @Test
    void testAttachComponentToRoom() {
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(componentRepository.findByName(component.getName())).thenReturn(Optional.of(component));

        var result = componentService.attachToRoom(component.getName(), room.getName());
        assertThat(result.isOk()).isTrue();
        assertThat(room.getPriceComponent()).isEqualTo(component);
        verify(roomRepository).save(room);
    }

    @Test
    void testAttachComponentToRoomFailsWhenRoomDoesNotExist() {
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.empty());

        var result = componentService.attachToRoom(component.getName(), room.getName());
        assertThat(result.isErr()).isTrue();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(roomRepository, never()).save(room);
    }

    @Test
    void testAttachComponentToRoomFailsWhenComponentDoesNotExist() {
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(componentRepository.findByName(component.getName())).thenReturn(Optional.empty());

        var result = componentService.attachToRoom(component.getName(), room.getName());
        assertThat(result.isErr()).isTrue();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(roomRepository, never()).save(room);
    }

    @Test
    void testAttachComponentToScreening() {
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(),
                screening.getStartTime())).thenReturn(Optional.of(screening));
        when(componentRepository.findByName(component.getName())).thenReturn(Optional.of(component));

        var result = componentService.attachToScreening(component.getName(), movie.getTitle(), room.getName(),
                format(screening.getStartTime()));
        assertThat(result.isOk()).isTrue();
        assertThat(screening.getPriceComponent()).isEqualTo(component);
        verify(screeningRepository).save(screening);
    }

    @Test
    void testAttachComponentToScreeningFailsWhenScreeningDoesNotExist() {
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(),
                screening.getStartTime())).thenReturn(Optional.empty());

        var result = componentService.attachToScreening(component.getName(), movie.getTitle(), room.getName(),
                format(screening.getStartTime()));
        assertThat(result.isErr()).isTrue();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(screeningRepository, never()).save(screening);
    }

    @Test
    void testAttachComponentToScreeningFailsWhenComponentDoesNotExist() {
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(),
                screening.getStartTime())).thenReturn(Optional.of(screening));
        when(componentRepository.findByName(component.getName())).thenReturn(Optional.empty());

        var result = componentService.attachToScreening(component.getName(), movie.getTitle(), room.getName(),
                format(screening.getStartTime()));
        assertThat(result.isErr()).isTrue();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(screeningRepository, never()).save(screening);
    }
}
