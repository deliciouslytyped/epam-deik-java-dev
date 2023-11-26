package com.epam.training.ticketservice.core.room.model;

import com.epam.training.ticketservice.core.room.persistence.Room;

public record RoomDto(String name, int row, int col) {
    public RoomDto(Room room) {
        this(room.getName(), room.getRows(), room.getCols());
    }
}
