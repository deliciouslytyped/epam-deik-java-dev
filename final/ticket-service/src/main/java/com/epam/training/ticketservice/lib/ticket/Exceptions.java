package com.epam.training.ticketservice.lib.ticket;

import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.ticket.model.TicketDto;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;

public class Exceptions {
    public static void throwAlreadyExists(TicketDto arg){
        throw new AlreadyExistsException("The ticket " + arg.getTicketId());
    }

    public static void throwNotExist(TicketDto arg){
        throw new ApplicationDomainException("The ticket " + arg.getTicketId() + " does not exist.");
    }

    public static void throwNotExistWithContext(Long id, Exception e) {
        throw new ApplicationDomainException("The ticket " + id + " does not exist.", e);
    }
}
