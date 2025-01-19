package com.pavan.hotel_service.service;

import com.pavan.hotel_service.client.RoomClient;
import com.pavan.hotel_service.model.Hotel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HotelService {
    Hotel addHotel(Hotel hotel);
    Hotel findById(Integer id);
    List<Hotel> findAll();
    Hotel updateHotel(Integer id, Hotel updatedHotel);
    Hotel partiallyUpdateHotel(Integer id, Hotel partialUpdateHotel);
    void deleteHotel(Integer id);

    List<Hotel> UpdateHotelWithRooms(RoomClient roomClient);
}
