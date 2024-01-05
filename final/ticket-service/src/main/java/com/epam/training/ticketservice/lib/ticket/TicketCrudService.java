package com.epam.training.ticketservice.lib.ticket;

import com.epam.training.ticketservice.lib.ticket.model.TicketDto;
import com.epam.training.ticketservice.lib.ticket.model.TicketMapper;
import com.epam.training.ticketservice.lib.ticket.persistence.Ticket;
import com.epam.training.ticketservice.support.CustomCrudService;

import java.util.List;

public interface TicketCrudService extends CustomCrudService<TicketDto, Long, TicketMapper> {}