package com.epam.training.ticketservice.lib.reservation;

import com.epam.training.ticketservice.lib.reservation.model.ReservationDto;
import com.epam.training.ticketservice.lib.reservation.model.ReservationMapper;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import com.epam.training.ticketservice.lib.ticket.model.TicketDto;
import com.epam.training.ticketservice.support.CustomCrudService;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ReservationCrudService extends CustomCrudService<ReservationDto, ReservationKey, ReservationMapper> {
    List<ReservationDto> findAllByTicketId(Long ticketId);

    void addSeat(@NonNull TicketDto t, @NonNull ScreeningDto s, int row, int col);
}