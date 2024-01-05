package com.epam.training.ticketservice.lib.room;

import com.epam.training.ticketservice.lib.room.model.RoomDto;
import com.epam.training.ticketservice.lib.room.model.RoomMapper;
import com.epam.training.ticketservice.support.CustomCrudService;

public interface RoomCrudService extends CustomCrudService<RoomDto, String, RoomMapper> {}
