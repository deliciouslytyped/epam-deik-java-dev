package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.dto.BookingDto;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.Seat;
import com.epam.training.ticketservice.util.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Result<BookingDto, OperationException> createBooking(String movieTitle, String roomName, String startTime,
                                                         String seats);

    Result<String, OperationException> viewPricing(String movieTitle, String roomName, String startTime, String seats);
}
