package com.epam.training.ticketservice.lib.seat.persistence;

import com.epam.training.ticketservice.lib.room.persistence.Room;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Embeddable
public class Seat implements Serializable {
    public Integer row;
    public Integer col;
}
