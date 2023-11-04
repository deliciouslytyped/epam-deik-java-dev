package com.epam.training.ticketservice.core.room;

import org.springframework.stereotype.Service;

import java.util.List;

public interface RoomService {

    public void createRoom(String name, int rows, int cols);

    public void updateRoom(String name, int rows, int cols);

    public void deleteRoom(String name);

    public List<Room> listRoom();
}
