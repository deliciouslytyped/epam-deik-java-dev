package com.epam.training.ticketservice.lib.reservation;

import com.epam.training.ticketservice.lib.reservation.model.ReservationDto;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.ticket.model.TicketDto;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;

public class Exceptions {
    public static void throwAlreadyExists(ReservationDto arg){
        throw new AlreadyExistsException("The reservation " + arg.toString());
    }

    public static void throwNotExist(ReservationDto arg){
        throw new ApplicationDomainException("The reservation " + arg.toString() + " does not exist.");
    }

    public static void throwNotExistWithContext(ReservationKey id, Exception e) {
        throw new ApplicationDomainException("The reservation " + id + " does not exist.", e);
    }
}
