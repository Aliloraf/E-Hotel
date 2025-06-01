package com.example.EHotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
import com.example.EHotel.model.hotel.Room;
import com.example.EHotel.repositories.hotel.RoomRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import org.springframework.data.domain.Sort;

@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAvailableRooms(RoomSearchCriteriaDTO criteria) {
        return roomRepository.findAvailableRooms(criteria);
    }

    public Room findRoomById(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    public List<Room> findRoomsByHotelId(int hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    @SuppressWarnings("null")
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }
    
    public void deleteRoom(int id) {
        roomRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    public Integer findUnusedId() {
        List<Room> rooms = roomRepository.findAll(Sort.by("idRoom"));
        if (!rooms.isEmpty()) {
            int expectedId = rooms.get(rooms.size() - 1).getIdRoom() + 1;
            return expectedId;
        } else {
            return 1;
        }
    }
}

