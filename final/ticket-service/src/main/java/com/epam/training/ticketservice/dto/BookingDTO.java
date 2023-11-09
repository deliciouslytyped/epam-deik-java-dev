package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Seat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BookingDTO {
    private final UserDTO user;
    private final List<Seat> seats;
}
