package com.example.EHotel.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EHotel.dtos.CreateRoomDTO;
import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
import com.example.EHotel.exceptions.ResourceNotFoundException;
import com.example.EHotel.model.hotel.Room;
import com.example.EHotel.repositories.hotel.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelService hotelService;

    @Transactional(readOnly = true)
    public List<Room> findAvailableRooms(RoomSearchCriteriaDTO criteria) {
        return roomRepository.findAvailableRooms(criteria);
    }

    @Transactional(readOnly = true)
    public Room getRoom(int id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chambre introuvable : " + id));
    }

    @Transactional(readOnly = true)
    public List<Room> findRoomsByHotelId(int hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    public Room addRoom(CreateRoomDTO dto) {
        Room room = new Room();
        room.setHotel(hotelService.getHotel(dto.getIdHotel()));
        copyFields(dto, room);
        return roomRepository.save(room);
    }

    /** Updates a room's attributes. The hotel of an existing room cannot be changed. */
    public Room updateRoom(int id, CreateRoomDTO dto) {
        Room room = getRoom(id);
        copyFields(dto, room);
        return roomRepository.save(room);
    }

    /** Deletes a room and returns the id of the hotel it belonged to. Its bookings and rentals are cascaded. */
    public int deleteRoom(int id) {
        Room room = getRoom(id);
        int hotelId = room.getHotel().getIdHotel();
        roomRepository.delete(room);
        roomRepository.flush();
        return hotelId;
    }

    public CreateRoomDTO toDto(Room room) {
        return new CreateRoomDTO(room.getIdRoom(), room.getRoomNumber(), room.getAvailability(), room.getPrice(),
                room.getView(), room.getExtensible(), room.getCapacity(), room.getHotel().getIdHotel());
    }

    private void copyFields(CreateRoomDTO dto, Room room) {
        room.setRoomNumber(dto.getRoomNumber());
        room.setAvailability(dto.getAvailability());
        room.setPrice(dto.getPrice());
        room.setView(dto.getView());
        room.setExtensible(dto.getExtensible());
        room.setCapacity(dto.getCapacity());
    }
}
