package com.epam.training.ticketservice.lib.reservation.persistence;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.seat.persistence.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
// And this is where the abstraction gets leaky...
//  with unique constraints involving columns/attributes/fields of an @Embedded object
//  and you apparently can't pass a (public static final) String[] to an annotation because of
//  compile time things, so I can't even just keep the strings inside the Seat class.
@Table(
        name = "reservation",
        uniqueConstraints = { @UniqueConstraint(name = "no_seat_collision", columnNames = {"rowIdx", "colIdx", "SCREENING_SCREENING_ID"}) })
//TODO composite key
//TODO this is a mess right now
public class Reservation implements Serializable {
    //TODO Im not sure this constraint is actually constructed correctly, RE: the RESERVATION. qualified names in the end row.
    @AttributeOverrides({
        @AttributeOverride(name = "rowIdx", column = @Column(
            columnDefinition = "integer not null check (\n" +
                "(0 < COL_IDX) AND ( COL_IDX <= (SELECT R.COL_COUNT FROM RESERVATION RE\n" +
                " INNER JOIN SCREENING S ON RE.SCREENING_SCREENING_ID = S.SCREENING_ID\n" +
                " INNER JOIN ROOM R ON S.ROOM_NAME = R.NAME\n" +
                    " WHERE (RESERVATION.COL_IDX, RESERVATION.ROW_IDX, RESERVATION.SCREENING_SCREENING_ID, RESERVATION.BOOKING_TICKET_ID) = (RE.COL_IDX, RE.ROW_IDX, RE.SCREENING_SCREENING_ID, RE.BOOKING_TICKET_ID))))"))
    })
    @Id
    public ReservationKey reservationFor;

}
