package com.epam.training.ticketservice.lib.ticket.model;

import com.epam.training.ticketservice.lib.reservation.model.ReservationDto;
import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class TicketDto {
    private final Long ticketId;
    private final Integer paid;
    private final ScreeningDto screening;
    private List<ReservationDto> reservations; // null if created in the presentation layer, initialized if enity from service layer
}


