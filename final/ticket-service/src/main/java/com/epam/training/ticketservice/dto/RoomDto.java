package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Room;
import lombok.Getter;

@Getter
public class RoomDto {
    private final String name;
    private final int rows;
    private final int columns;

    public RoomDto(Room dao) {
        this.name = dao.getName();
        this.rows = dao.getRows();
        this.columns = dao.getColumns();
    }

    @Override
    public String toString() {
        return "Room " + name + " with " + (rows * columns) + " seats, " + rows + " rows and " + columns + " columns";
    }
}
