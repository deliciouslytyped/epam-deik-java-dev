package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.component.AuthenticationHolder;
import com.epam.training.ticketservice.component.BasePriceHolder;
import com.epam.training.ticketservice.component.PriceCalculator;
import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.Seat;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.repository.MovieRepository;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.repository.ScreeningRepository;
import com.epam.training.ticketservice.repository.UserRepository;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final AuthenticationHolder authHolder;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PriceCalculator calculator;
    private final BasePriceHolder basePriceHolder;

    @Override
    public Result<BookingDto, OperationException> createBooking(String movieTitle, String roomName, String startTime,
                                                                String seatsRaw) {
        var movie = movieRepository.findByTitle(movieTitle);
        if (movie.isEmpty()) {
            return Result.err(new NotFoundException("Movie"));
        }
        var room = roomRepository.findByName(roomName);
        if (room.isEmpty()) {
            return Result.err(new NotFoundException("Room"));
        }
        var screening = screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movieTitle, roomName,
                LocalDateTime.parse(startTime, Screening.TIME_FORMAT));
        if (screening.isEmpty()) {
            return Result.err(new NotFoundException("Screening"));
        }
        var seats = Seat.fromString(seatsRaw);

        var invalidSeat = hasInvalidSeat(seats, room.get());
        if (invalidSeat.isPresent()) {
            return Result.err(new OperationException("Seat " + invalidSeat.get() + " does not exist in this room"));
        }

        var takenSeat = hasAlreadyTakenSeat(seats, screening.get());
        if (takenSeat.isPresent()) {
            return Result.err(new OperationException("Seat " + takenSeat.get() + " is already taken"));
        }

        var username = authHolder.getAuthentication().getName();
        var booking = new Booking(userRepository.findByUsername(username).get(), screening.get(), seatsRaw,
                calculator.calculate(screening.get(), basePriceHolder.getBasePrice(), seats.size()));
        bookingRepository.save(booking);
        return Result.ok(new BookingDto(booking));
    }

    @Override
    public Result<String, OperationException> viewPricing(String movieTitle, String roomName, String startTime,
                                                          String seatsRaw) {
        var screening = screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movieTitle, roomName,
                LocalDateTime.parse(startTime, Screening.TIME_FORMAT));
        if (screening.isEmpty()) {
            return Result.err(new NotFoundException("Screening"));
        }
        var seats = Seat.fromString(seatsRaw);
        int price = calculator.calculate(screening.get(), basePriceHolder.getBasePrice(), seats.size());
        return Result.ok("The price for this booking would be " + price + " HUF");
    }

    private Optional<Seat> hasAlreadyTakenSeat(List<Seat> seats, Screening screening) {
        return screening.getBookings().stream()
                .flatMap(b -> Seat.fromString(b.getSeats()).stream())
                .filter(seats::contains)
                .findFirst();
    }

    private Optional<Seat> hasInvalidSeat(List<Seat> seats, Room room) {
        return seats.stream()
                .filter(s -> !s.inBounds(room.getRows(), room.getColumns())).findFirst();
    }
}
