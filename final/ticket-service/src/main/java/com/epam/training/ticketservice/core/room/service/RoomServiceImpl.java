package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(String name, int rows, int cols) { //TODO Room argument
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
    @Transactional
    public void deleteRoom(String name) {
        roomRepository.deleteByName(name);
    }

    @Override
    public Optional<RoomDto> findRoom(String name) {
        Optional<Room> room = roomRepository.findByName(name);
        return room.map(value -> new RoomDto(value.getName(),value.getRows(),value.getCols()));
    }

    @Override
    public List<RoomDto> listRoom() {
        return roomRepository.findAll().stream()
                .map(room -> new RoomDto(room.getName(), room.getRows(), room.getCols()))
                .toList();
    }
}
