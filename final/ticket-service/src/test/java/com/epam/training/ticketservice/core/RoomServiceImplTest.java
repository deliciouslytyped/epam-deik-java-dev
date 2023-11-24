package com.epam.training.ticketservice.core;

import com.epam.training.ticketservice.core.room.Room;
import com.epam.training.ticketservice.core.room.RoomRepository;
import com.epam.training.ticketservice.core.room.RoomServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class RoomServiceImplTest {

    private static final Room ENTITY = new Room("szoba",10,10);

    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final RoomServiceImpl underTest = new RoomServiceImpl(roomRepository);
    @Test
    void testFindRoomShouldReturnSzobaWhenInputRoomNameIsSzoba() {
        //Given
        when(roomRepository.findByName("szoba")).thenReturn(Optional.of(ENTITY));
        Optional<Room> expected = Optional.of(ENTITY);

        //When
        Optional<Room> actual = underTest.findRoom("szoba");

        //Then
        assertEquals(expected,actual);
        verify(roomRepository).findByName("szoba");
    }

    @Test
    void testFindRoomShouldReturnOptionalEmptyWhenInputRoomNameDoesNotExist() {
        // Given
        when(roomRepository.findByName("dummy")).thenReturn(Optional.empty());
        Optional<Room> expected = Optional.empty();

        // When
        Optional<Room> actual = underTest.findRoom("dummy");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(roomRepository).findByName("dummy");
    }

    @Test
    void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameIsNull() {
        // Given
        when(roomRepository.findByName(null)).thenReturn(Optional.empty());
        Optional<Room> expected = Optional.empty();

        // When
        Optional<Room> actual = underTest.findRoom(null);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(roomRepository).findByName(null);
    }

    @Test
    void testCreateRoomShouldStoreTheGivenRoomWhenInputRoomIsValid() {
        //Given
        when(roomRepository.save(ENTITY)).thenReturn(ENTITY);

        //When
        underTest.createRoom(ENTITY.getName(),ENTITY.getRows(),ENTITY.getCols());

        //Then
        verify(roomRepository).save(ENTITY);
    }

    @Test
    void testListRoomShouldReturnTheAvailableRooms() {
        //Given
        when(roomRepository.findAll()).thenReturn(List.of(ENTITY));

        //When
        List<Room> actual = underTest.listRoom();

        //Then
        verify(roomRepository).findAll();
        assertEquals(1, actual.size());
    }

    @Test
    void testDeleteRoomShouldDeleteRoomWhenTheRoomExist() {
        //TODO
    }

    @Test
    void testUpdateRoomShouldUpdateTheRoom() {
        //Given
        when(roomRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));

        //When
        underTest.updateRoom(ENTITY.getName(),15,15);
        //Then
        assertEquals(ENTITY,new Room("szoba",15,15));
    }


    }

