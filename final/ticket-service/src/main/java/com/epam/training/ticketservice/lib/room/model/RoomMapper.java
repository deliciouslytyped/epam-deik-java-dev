package com.epam.training.ticketservice.lib.room.model;

import com.epam.training.ticketservice.lib.room.persistence.Room;
import com.epam.training.ticketservice.support.CustomMapper;
import org.mapstruct.Mapper;

@Mapper
public interface RoomMapper extends CustomMapper<RoomDto, Room> {
}
