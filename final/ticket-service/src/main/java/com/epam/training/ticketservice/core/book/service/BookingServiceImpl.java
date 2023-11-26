package com.epam.training.ticketservice.core.book.service;

import com.epam.training.ticketservice.core.book.persistence.Booking;
import com.epam.training.ticketservice.core.book.persistence.BookingRepository;
import com.epam.training.ticketservice.core.book.persistence.Seat;
import com.epam.training.ticketservice.core.book.model.SeatDto;
import com.epam.training.ticketservice.core.book.model.BookingDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import com.epam.training.ticketservice.core.user.persistence.User;
import com.epam.training.ticketservice.core.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ScreeningRepository screeningRepository;
    private int basePrice = 1500;


    @Override
    public BookingDto createBook(String username, String movieName, String roomName, String date, List<SeatDto> seats) {
        Screening screening = new Screening(movieName,roomName,date);
        screeningRepository.findByMovieNameAndRoomNameAndDate(screening.getMovieName(),
                screening.getRoomName(),screening.getDate());
        bookingRepository.findAllByScreening(screening).stream()
                .map(booking -> booking.getSeats().stream().map(SeatDto::new).toList())
                .flatMap(Collection::stream)
                .forEach(occupiedSeat -> {
                    if (seats.contains(occupiedSeat)) {
                        throw new IllegalArgumentException("Seat " + occupiedSeat + " is already taken");
                    }
                });
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid user!"); //TODO
        }

        int sumPrice = seats.size() * basePrice;

        Booking booking = new Booking(user.get(),screening,seats.stream().map(Seat::new).toList(),sumPrice);
        bookingRepository.save(booking);
        return new BookingDto(booking);
    }

    @Override
    public List<BookingDto> listBookings(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid user");//TODO
        }
        return bookingRepository.findAllByUser(user.get()).stream().map(BookingDto::new).toList();
    }


    @Override
    public void updateBasePrice(int newBasePrice) {
        this.basePrice = newBasePrice;
    }

    @Override
    public int getBasePrice() {
        return basePrice;
    }
}
