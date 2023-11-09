package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.util.Result;

import java.util.List;

public interface RoomService {

    Result<?, OperationException> createRoom(String name, int rows, int columns);

    Result<?, OperationException> updateRoom(String name, int rows, int columns);

    Result<?, OperationException> deleteRoom(String name);

    Result<List<RoomDto>, OperationException> listRooms();
}
