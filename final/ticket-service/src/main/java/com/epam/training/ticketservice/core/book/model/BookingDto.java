package com.epam.training.ticketservice.core.book.model;

import com.epam.training.ticketservice.core.book.persistence.Booking;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.util.List;

public record BookingDto(ScreeningDto screening, List<SeatDto> seats, int price) {
    public BookingDto(Booking booking) {
        this(new ScreeningDto(booking.getScreening()),booking.getSeats().stream().map(SeatDto::new)
                .toList(), booking.getPrice());
    }
}
