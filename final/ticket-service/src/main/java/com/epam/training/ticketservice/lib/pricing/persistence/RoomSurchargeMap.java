package com.epam.training.ticketservice.lib.pricing.persistence;

import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.pricing.persistence.base.SurchargeMap;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

// TODO figure out what the correct setting is here
//  https://stackoverflow.com/questions/38572566/warning-equals-hashcode-on-lomboks-data-annotation-with-inheritance
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
public class RoomSurchargeMap extends SurchargeMap {
    @ManyToOne
    public Room room;

    public RoomSurchargeMap(Long id, Surcharge s, Room room){
        super(id, s);
        this.room = room;
    }
}
