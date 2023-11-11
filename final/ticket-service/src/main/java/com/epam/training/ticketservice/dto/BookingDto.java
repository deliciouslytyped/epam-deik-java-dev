package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Seat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class BookingDto {
    private final ScreeningDto screening;
    private final List<Seat> seats;
    private final int price;

    public BookingDto(Booking dao) {
        this.screening = new ScreeningDto(dao.getScreening());
        this.seats = Seat.fromString(dao.getSeats());
        this.price = dao.getPrice();
    }

    @Override
    public String toString() {
        return "Seats " + String.join(", ", seats.stream().map(Seat::toString).toList())
                + " on " + screening.getMovie().getTitle()
                + " in room " + screening.getRoomName()
                + " starting at " + screening.getStartTime() + " for " + price + " HUF";
    }
}
