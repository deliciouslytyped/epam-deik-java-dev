package com.epam.training.ticketservice.lib.reservation.persistence;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.seat.persistence.Seat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Data
@Entity
// And this is where the abstraction gets leaky...
//  with unique constraints involving columns/attributes/fields of an @Embedded object
//  and you apparently can't pass a (public static final) String[] to an annotation because of
//  compile time things, so I can't even just keep the strings inside the Seat class.
@Table(
        name = "reservation",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"row", "col"}) })

//TODO composite key
public class Reservation implements Serializable {
    @Id
    @ManyToOne
    public Booking booking;

    @Id
    @ManyToOne
    public Screening screening;

    public Seat seat;
}
