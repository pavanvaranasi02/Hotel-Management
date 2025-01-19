package com.pavan.room_service.service;

import com.pavan.room_service.exception.InvalidRoomDataException;
import com.pavan.room_service.exception.RoomNotFoundException;
import com.pavan.room_service.model.Room;
import com.pavan.room_service.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        room = new Room(1, "Deluxe", 101, 1500.0, 2, true, "WiFi", 1);
    }

    @Test
    void testGetAllRooms() {
        // Arrange
        Room room1 = new Room(1, "Suite", 101, 2000.0, 2, true, "WiFi, Breakfast", 1);
        Room room2 = new Room(2, "Deluxe", 102, 1500.0, 2, true, "WiFi", 1);
        List<Room> roomList = new ArrayList<>();
        roomList.add(room1);
        roomList.add(room2);

        when(roomRepository.findAll()).thenReturn(roomList);

        // Act
        List<Room> rooms = roomServiceImpl.getAllRooms();

        // Assert
        assertEquals(2, rooms.size());
        assertEquals(roomList, rooms);
        verify(roomRepository, times(1)).findAll(); // Ensure the method is called once
    }

    @Test
    void testGetRoomsByHotelId() {
        // Arrange
        Room room1 = new Room(1, "Suite", 101, 2000.0, 2, true, "WiFi, Breakfast", 1);
        Room room2 = new Room(2, "Deluxe", 102, 1500.0, 2, true, "WiFi", 1);
        List<Room> roomList = new ArrayList<>();
        roomList.add(room1);
        roomList.add(room2);

        when(roomRepository.findByHotelId(1)).thenReturn(roomList);

        // Act
        List<Room> rooms = roomServiceImpl.getRoomsByHotelId(1);

        // Assert
        assertEquals(2, rooms.size());
        assertEquals(roomList, rooms);
        verify(roomRepository, times(1)).findByHotelId(1); // Ensure the method is called once
    }



    @Test
    void testAddRoom_Success() {
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room savedRoom = roomServiceImpl.addRoom(room);

        assertEquals(room.getId(), savedRoom.getId());
        assertEquals(room.getRoomType(), savedRoom.getRoomType());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testAddRoom_InvalidPrice() {
        Room invalidRoom = new Room(2, "Standard", 102, -500.0, 2, true, "WiFi", 1);

        InvalidRoomDataException exception = assertThrows(InvalidRoomDataException.class, () -> {
            roomServiceImpl.addRoom(invalidRoom);
        });

        assertEquals("Price must be greater than zero", exception.getMessage());
        verify(roomRepository, never()).save(any());
    }

    @Test
    void testGetRoomById_Success() {
        when(roomRepository.findById(1)).thenReturn(Optional.of(room));

        Room foundRoom = roomServiceImpl.getRoomById(1);

        assertEquals(room.getId(), foundRoom.getId());
        verify(roomRepository, times(1)).findById(1);
    }

    @Test
    void testGetRoomById_NotFound() {
        when(roomRepository.findById(1)).thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class, () -> {
            roomServiceImpl.getRoomById(1);
        });

        assertEquals("Room with id 1 not found!", exception.getMessage());
        verify(roomRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateRoom_Success() {
        // Arrange
        Room existingRoom = new Room(1, "Standard", 101, 1500.0, 2, true, "WiFi", 1);
        Room updatedRoom = new Room(1, "Suite", 101, 2000.0, 2, true, "WiFi, Breakfast", 1);

        // Mock the behavior of the repository
        when(roomRepository.findById(1)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);

        // Act
        Room savedRoom = roomServiceImpl.updateRoom(1, updatedRoom);

        // Assert
        assertEquals(updatedRoom.getRoomType(), savedRoom.getRoomType());
        assertEquals(updatedRoom.getPrice(), savedRoom.getPrice());
        assertEquals(updatedRoom.getFacilities(), savedRoom.getFacilities());
        verify(roomRepository, times(1)).findById(1); // Ensure findById is called once
        verify(roomRepository, times(1)).save(any(Room.class)); // Ensure save is called once
    }



    @Test
    void testUpdateRoom_NotFound() {
        Room updatedRoom = new Room(1, "Suite", 101, 2000.0, 2, true, "WiFi, Breakfast", 1);
        when(roomRepository.findById(1)).thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class, () -> {
            roomServiceImpl.updateRoom(1, updatedRoom);
        });

        assertEquals("Room with id 1 not found!", exception.getMessage());
        verify(roomRepository, never()).save(any());
    }

    @Test
    void testPartiallyUpdateRoom_Success() {
        Room partialUpdateRoom = new Room(1, null, null, 1800.0, null, null, null, null);
        when(roomRepository.findById(1)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room updatedRoom = roomServiceImpl.partiallyUpdateRoom(1, partialUpdateRoom);

        assertEquals(room.getPrice(), updatedRoom.getPrice());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testPartiallyUpdateRoom_NotFound() {
        Room partialUpdateRoom = new Room(1, null, null, 1800.0, null, null, null, null);
        when(roomRepository.findById(1)).thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class, () -> {
            roomServiceImpl.partiallyUpdateRoom(1, partialUpdateRoom);
        });

        assertEquals("Room with id 1 not found!", exception.getMessage());
        verify(roomRepository, never()).save(any());
    }

    @Test
    void testDeleteRoom_Success() {
        when(roomRepository.findById(1)).thenReturn(Optional.of(room));

        roomServiceImpl.deleteRoom(1);

        verify(roomRepository, times(1)).delete(room);
    }

    @Test
    void testDeleteRoom_NotFound() {
        when(roomRepository.findById(1)).thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class, () -> {
            roomServiceImpl.deleteRoom(1);
        });

        assertEquals("Room with id 1 not found!", exception.getMessage());
        verify(roomRepository, never()).delete(any());
    }
}
