package com.epam.training.ticketservice.lib.reservation.persistence;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.seat.persistence.Seat;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="reservation")

public class Reservation {
    @Id
    @ManyToOne
    public Booking booking;

    @Id
    @ManyToOne
    public Screening screening;

    @OneToOne
    public Seat seat;
}
