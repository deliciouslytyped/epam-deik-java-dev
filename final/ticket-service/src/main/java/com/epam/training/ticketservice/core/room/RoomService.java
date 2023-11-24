package com.epam.training.ticketservice.core.room;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    void createRoom(String name, int rows, int cols);

    void updateRoom(String name, int rows, int cols);

    void deleteRoom(String name);
    
    Optional<Room> findRoom(String name);

    List<Room> listRoom();
}
