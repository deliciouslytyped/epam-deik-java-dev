package com.epam.training.ticketservice.lib.reservation.model;

import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import lombok.Data;

@Data
public class ReservationDto {
    private final Long ticketId;
    private final ScreeningDto screening;
    private final SeatDto seat;
}
