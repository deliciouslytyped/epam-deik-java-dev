package com.epam.training.ticketservice.lib.reservation.persistence;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.seat.persistence.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ReservationKey implements Serializable {
    @ManyToOne
    public Booking booking;

    @ManyToOne
    public Screening screening;

    //Embedded
    @Embedded
    public Seat seat;
}
