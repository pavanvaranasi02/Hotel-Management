package com.pavan.room_service.service;

import com.pavan.room_service.model.Room;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {
    List<Room> getAllRooms();
    Room getRoomById(Integer id);
    List<Room> getRoomsByHotelId(Integer hotelId);
    Room addRoom(Room room);
    Room updateRoom(Integer id, Room updatedRoom);
    Room partiallyUpdateRoom(Integer id, Room partialUpdateRoom);
    void deleteRoom(Integer id);
}
