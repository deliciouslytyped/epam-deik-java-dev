package com.epam.training.ticketservice.lib.pricing.persistence;

import com.epam.training.ticketservice.lib.room.persistence.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

// TODO figure out what the correct setting is here
//  https://stackoverflow.com/questions/38572566/warning-equals-hashcode-on-lomboks-data-annotation-with-inheritance
@EqualsAndHashCode(callSuper = true)
@Data
public class RoomSurchargeMap extends SurchargeMap {
    @ManyToOne
    public Room room;
}
