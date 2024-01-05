package com.epam.training.ticketservice.lib.reservation.persistence;

import com.epam.training.ticketservice.lib.room.persistence.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Seat implements Serializable {
    /* See Reservation
    // So we can use UniqueConstraint without depending on string literals not changing
    // TODO some kind of reflection for this?
    public static final String[] columnNames = {"row", "col"};
    */

    public Integer rowIdx;
    public Integer colIdx;
}
