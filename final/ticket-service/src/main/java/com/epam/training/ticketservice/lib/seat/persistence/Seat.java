package com.epam.training.ticketservice.lib.seat.persistence;

import com.epam.training.ticketservice.lib.room.persistence.Room;
import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class Seat implements Serializable {
    /* See Reservation
    // So we can use UniqueConstraint without depending on string literals not changing
    // TODO some kind of reflection for this?
    public static final String[] columnNames = {"row", "col"};
    */

    public Integer row;
    public Integer col;
}
