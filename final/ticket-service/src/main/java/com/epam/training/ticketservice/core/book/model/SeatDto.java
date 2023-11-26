package com.epam.training.ticketservice.core.book.model;

import com.epam.training.ticketservice.core.book.persistence.Seat;

public record SeatDto(int row, int col) {
    public SeatDto(Seat seat) {
        this(seat.getRows(), seat.getCols());
    }
}
