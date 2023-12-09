package com.epam.training.ticketservice.lib.room.model;

import com.epam.training.ticketservice.lib.room.persistence.Room;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class RoomDto {
    private final String name;
    private final Integer rowCount;
    private final Integer colCount;

    public RoomDto(Room r){
        name = r.getName();
        rowCount = r.getRowCount();
        colCount = r.getColCount();
    }
}