package com.epam.training.ticketservice.lib.ticket.persistence;

import com.epam.training.ticketservice.support.jparepo.UpdateByEntityFragment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketCrudRepository extends JpaRepository<Ticket, Long>, UpdateByEntityFragment<Ticket> {}