package com.epam.training.ticketservice.test.service;

import com.epam.training.ticketservice.dto.RoomDto;
import com.epam.training.ticketservice.exception.AlreadyExistsException;
import com.epam.training.ticketservice.exception.NotFoundException;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTests {
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;

    @BeforeEach
    void setup() {
        room = new Room("Room", 10, 10);
    }

    @Test
    void testRoomListing() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        var result = roomService.listRooms();
        assertThat(result.isOk()).isTrue();
        assertThat(result.result()).containsExactly(new RoomDto(room));
    }

    @Test
    void testRoomCreation() {
        when(roomRepository.existsByNameIgnoreCase(room.getName())).thenReturn(false);

        var result = roomService.createRoom(room.getName(), room.getRows(), room.getColumns());
        assertThat(result.isOk()).isTrue();
        verify(roomRepository).save(room);
    }

    @Test
    void testRoomCreationFailsWhenRoomAlreadyExists() {
        when(roomRepository.existsByNameIgnoreCase(room.getName())).thenReturn(true);

        var result = roomService.createRoom(room.getName(), room.getRows(), room.getColumns());
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(AlreadyExistsException.class);
    }

    @Test
    void testRoomDeletion() {
        when(roomRepository.existsByNameIgnoreCase(room.getName())).thenReturn(true);

        var result = roomService.deleteRoom(room.getName());
        assertThat(result.isOk()).isTrue();
        verify(roomRepository).deleteByName(room.getName());
    }

    @Test
    void testRoomDeletionFailsWhenRoomDoesNotExist() {
        when(roomRepository.existsByNameIgnoreCase(room.getName())).thenReturn(false);

        var result = roomService.deleteRoom(room.getName());
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
    }

    @Test
    void testRoomUpdate() {
        var room = spy(this.room);
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.of(room));

        var result = roomService.updateRoom(room.getName(), room.getRows(), room.getColumns());
        assertThat(result.isOk()).isTrue();
        verify(roomRepository).save(room);
        verify(room).setRows(anyInt());
        verify(room).setColumns(anyInt());
    }

    @Test
    void testRoomUpdateFailsWhenRoomDoesNotExist() {
        when(roomRepository.findByName(room.getName())).thenReturn(Optional.empty());

        var result = roomService.updateRoom(room.getName(), room.getRows(), room.getColumns());
        assertThat(result.isOk()).isFalse();
        assertThat(result.error()).isInstanceOf(NotFoundException.class);
    }
}
