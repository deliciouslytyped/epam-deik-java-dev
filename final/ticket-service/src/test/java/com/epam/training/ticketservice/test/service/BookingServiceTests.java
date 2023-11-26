package com.epam.training.ticketservice.test.service;

import com.epam.training.ticketservice.component.AuthenticationHolder;
import com.epam.training.ticketservice.component.BasePriceHolder;
import com.epam.training.ticketservice.component.PriceCalculator;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.model.UserRole;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.repository.UserRepository;
import com.epam.training.ticketservice.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private AuthenticationHolder authHolder;

    @Mock
    private Authentication auth;

    private BookingServiceImpl bookingService;

    private Movie movie;
    private Room room;
    private Screening screening;
    private User user;

    private String format(LocalDateTime time) {
        return Screening.TIME_FORMAT.format(time);
    }

    @BeforeEach
    void setup() {
        PriceCalculator calculator = new PriceCalculator();
        BasePriceHolder basePriceHolder = new BasePriceHolder();
        bookingService = new BookingServiceImpl(authHolder, movieRepository, roomRepository, screeningRepository,
                userRepository, bookingRepository, calculator, basePriceHolder);

        movie = new Movie("Toy Story 3", "animation", 350);
        room = new Room("garage", 10, 10);
        screening = new Screening(movie, room, LocalDateTime.of(2023, 11, 1, 10, 0));
        user = new User("admin", "admin", UserRole.ADMIN);
    }

    @Test
    void testBookingCreation() {
        screening.setBookings(List.of());
        when(authHolder.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(user.getUsername());
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(),
                screening.getStartTime())).thenReturn(Optional.of(screening));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        var result = bookingService.createBooking(movie.getTitle(), room.getName(), format(screening.getStartTime()),
                "1,2 1,3");

        assertThat(result.isOk()).isTrue();
        verify(bookingRepository).save(any());
    }

    @Test
    void testBookingCreationFailWhenMovieDoesNotExist() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.empty());

        var result = bookingService.createBooking(movie.getTitle(), room.getName(), format(screening.getStartTime()),
                "1,2 1,3");

        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testBookingCreationFailWhenRoomDoesNotExist() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.empty());

        var result = bookingService.createBooking(movie.getTitle(), room.getName(), format(screening.getStartTime()),
                "1,2 1,3");

        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testBookingCreationFailWhenScreeningDoesNotExist() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(),
                screening.getStartTime())).thenReturn(Optional.empty());

        var result = bookingService.createBooking(movie.getTitle(), room.getName(), format(screening.getStartTime()),
                "1,2 1,3");

        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testBookingCreationFailWhenSeatIsInvalid() {
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(),
                screening.getStartTime())).thenReturn(Optional.of(screening));

        var result = bookingService.createBooking(movie.getTitle(), room.getName(), format(screening.getStartTime()),
                "1,2 1,3 1,11");

        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(OperationException.class);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testBookingCreationFailWhenSeatIsAlreadyTaken() {
        screening.setBookings(List.of(new Booking(user, screening, "1,2 1,3", 700)));
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(),
                screening.getStartTime())).thenReturn(Optional.of(screening));

        var result = bookingService.createBooking(movie.getTitle(), room.getName(), format(screening.getStartTime()),
                "1,2 1,3");

        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(OperationException.class);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testViewPricing() {
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(),
                screening.getStartTime())).thenReturn(Optional.of(screening));

        var result = bookingService.viewPricing(movie.getTitle(), room.getName(), format(screening.getStartTime()),
                "1,2 1,3");

        assertThat(result.isOk()).isTrue();
    }

    @Test
    void testViewPricingFailWhenScreeningDoesNotExist() {
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movie.getTitle(), room.getName(),
                screening.getStartTime())).thenReturn(Optional.empty());

        var result = bookingService.viewPricing(movie.getTitle(), room.getName(), format(screening.getStartTime()),
                "1,2 1,3");

        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
    }
}
