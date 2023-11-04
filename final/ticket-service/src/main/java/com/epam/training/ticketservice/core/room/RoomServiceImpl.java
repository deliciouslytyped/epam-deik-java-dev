package com.epam.training.ticketservice.core.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(String name, int rows, int cols) {
        roomRepository.save(new Room(name,rows,cols));
    }

    @Override
    public void updateRoom(String name, int rows, int cols) {
        Room room = roomRepository.findByName(name).get();
        room.setRows(rows);
        room.setCols(cols);
        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(String name) {
        roomRepository.deleteByName(name);
    }

    @Override
    public List<Room> listRoom() {
        return roomRepository.findAll();
    }
}
