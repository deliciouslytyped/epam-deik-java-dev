package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.model.UserRole;
import lombok.Getter;

import java.util.List;

@Getter
public class UserDto {
    private final String username;
    private final UserRole role;
    private final List<BookingDto> bookings;

    public UserDto(User dao) {
        this.username = dao.getUsername();
        this.role = dao.getRole();
        this.bookings = dao.getBookings().stream().map(BookingDto::new).toList();
    }
}
