package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.dto.RoomDTO;
import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.exception.OperationException;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.service.RoomService;
import com.epam.training.ticketservice.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository repository;

    @Override
    public Result<?, OperationException> createRoom(String name, int rows, int columns) {
        if (repository.existsByNameIgnoreCase(name)) return Result.err(new AlreadyExistsException("Room"));
        repository.save(new Room(name, rows, columns));
        return Result.ok(null);
    }

    @Override
    public Result<?, OperationException> updateRoom(String name, int rows, int columns) {
        var room = repository.findByName(name);
        if (room.isEmpty()) return Result.err(new NotFoundException("Room"));
        room.get().setRows(rows);
        room.get().setColumns(columns);
        repository.save(room.get());
        return Result.ok(null);
    }

    @Override
    public Result<?, OperationException> deleteRoom(String name) {
        if (!repository.existsByNameIgnoreCase(name)) return Result.err(new NotFoundException("Room"));
        repository.deleteByName(name);
        return Result.ok(null);
    }

    @Override
    public Result<List<RoomDTO>, OperationException> listRooms() {
        return Result.ok(repository.findAll().stream().map(RoomDTO::new).toList());
    }
}
