package com.epam.training.ticketservice.lib.room;

import com.epam.training.ticketservice.lib.room.persistence.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl {
    private final RoomRepository repository;
}
