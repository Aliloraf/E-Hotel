package com.example.EHotel.repositories.hotel;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
import com.example.EHotel.model.hotel.Room;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    
    @Query("SELECT r FROM Room r INNER JOIN r.hotel h WHERE r.availability = true and r.capacity = :#{#criteria.roomCapacity} and r.price <= :#{#criteria.maxPrice} and h.hotelChain.id = :#{#criteria.hotelChainId} and h.startNumber >= :#{#criteria.startNumber} and h.roomsNumber >= :#{#criteria.roomsNumber} and r.id NOT IN (SELECT b.room.id FROM Booking b WHERE b.startDate <= :#{#criteria.endDate} and b.endDate >= :#{#criteria.startDate})")
    List<Room> findAvailableRooms(@Param("criteria") RoomSearchCriteriaDTO criteria);

    Optional<Room> findById(int id);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId")
    List<Room> findByHotelId(@Param("hotelId") int hotelId);
}

