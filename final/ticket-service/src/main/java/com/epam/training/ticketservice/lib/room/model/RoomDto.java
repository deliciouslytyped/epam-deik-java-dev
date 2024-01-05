package com.epam.training.ticketservice.lib.room.model;

import com.epam.training.ticketservice.support.dto.DTO;
import lombok.Data;

@Data
public class RoomDto implements DTO<String> {
    private final String name;
    private final Integer rowCount;
    private final Integer colCount;

    @Override
    public String getKey() {
        return getName();
    }
}