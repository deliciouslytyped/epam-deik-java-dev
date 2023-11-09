package com.epam.training.ticketservice.service;

public interface RoomService {

    void createRoom(String name, int rows, int columns);

    void updateRoom(String name, int rows, int columns);

    void deleteRoom(String name);
}
