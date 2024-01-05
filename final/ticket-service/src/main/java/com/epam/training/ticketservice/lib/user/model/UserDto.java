package com.epam.training.ticketservice.lib.user.model;

import com.epam.training.ticketservice.lib.ticket.model.TicketDto;
import com.epam.training.ticketservice.lib.user.persistence.base.UserBase;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private final Long uid;
    private final String uname;
    List<TicketDto> bookings;  // null if created in the presentation layer, initialized if enity from service layer
}
