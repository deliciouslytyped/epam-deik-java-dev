package com.epam.training.ticketservice.lib.reservation.persistence;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import com.epam.training.ticketservice.lib.seat.persistence.Seat;
import com.epam.training.ticketservice.support.CheckConstraint;
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
@CheckConstraint(name="CHECK_COL", check= """
    (0 < COL_IDX) AND (COL_IDX <= (SELECT R.COL_COUNT
                                     FROM SCREENING S
                                       INNER JOIN ROOM R
                                         ON S.ROOM_NAME = R.NAME
                                     WHERE SCREENING_SCREENING_ID = S.SCREENING_ID))
                        """)
@CheckConstraint(name="CHECK_ROW", check= """
    (0 < ROW_IDX) AND (ROW_IDX <= (SELECT R.ROW_COUNT
                                     FROM SCREENING S
                                       INNER JOIN ROOM R
                                         ON S.ROOM_NAME = R.NAME
                                     WHERE SCREENING_SCREENING_ID = S.SCREENING_ID))
                        """)
@Table(
        name = "reservation",
        uniqueConstraints = { @UniqueConstraint(name = "no_seat_collision", columnNames = {"rowIdx", "colIdx", "SCREENING_SCREENING_ID"}) })
//TODO composite key
//TODO this is a mess right now
public class Reservation implements Serializable {
    @Id
    public ReservationKey reservationFor;

}