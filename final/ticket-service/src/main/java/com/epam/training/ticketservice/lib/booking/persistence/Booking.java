package com.epam.training.ticketservice.lib.booking.persistence;

import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.seat.persistence.Seat;
import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.util.Currency;
import java.util.Set;

/**
 * Reservations are handled through the reservations relation.
 */
@Data
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long ticketId;

    @ManyToOne
    public Screening screening;

    @Column(nullable = false)
    public Currency paid;
}
