package com.epam.training.ticketservice.lib.booking.persistence;

import com.epam.training.ticketservice.lib.reservation.persistence.Reservation;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.seat.persistence.Seat;
import lombok.Data;

import javax.persistence.*;
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
    @Column(nullable = false)
    public Screening screening;

    @Column(nullable = false)
    public Currency paid;
}
