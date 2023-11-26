package com.epam.training.ticketservice.core.book.service;

import com.epam.training.ticketservice.core.book.model.SeatDto;
import com.epam.training.ticketservice.core.book.model.BookingDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingDto createBook(String username, String movieName, String roomName, String date, List<SeatDto> seats);

    List<BookingDto> listBookings(String username);

    void updateBasePrice(int newBasePrice);

    int getBasePrice();
}
