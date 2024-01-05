package com.epam.training.ticketservice.lib.user.model;

import com.epam.training.ticketservice.lib.ticket.model.TicketDto;
import lombok.Data;

import java.util.List;

@Data
public class UserCreationDto {
    private final String username;
    private final String password;
}