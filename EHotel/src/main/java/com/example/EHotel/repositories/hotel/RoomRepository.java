package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
import com.example.EHotel.model.hotel.Room;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    /**
     * Rooms that are marked available and have no booking or rental overlapping the requested stay.
     * Every criterion other than the dates is optional (null means "any").
     */
    @Query("""
            SELECT r FROM Room r JOIN FETCH r.hotel h JOIN FETCH h.hotelChain c
            WHERE r.availability = true
              AND (:#{#criteria.roomCapacity} IS NULL OR r.capacity = :#{#criteria.roomCapacity})
              AND (:#{#criteria.maxPrice} IS NULL OR r.price <= :#{#criteria.maxPrice})
              AND (:#{#criteria.hotelChainId} IS NULL OR c.id = :#{#criteria.hotelChainId})
              AND (:#{#criteria.city} IS NULL OR h.city = :#{#criteria.city})
              AND (:#{#criteria.startNumber} IS NULL OR h.startNumber >= :#{#criteria.startNumber})
              AND (:#{#criteria.roomsNumber} IS NULL OR h.roomsNumber >= :#{#criteria.roomsNumber})
              AND NOT EXISTS (SELECT 1 FROM Booking b WHERE b.room = r
                              AND b.startDate < :#{#criteria.endDate} AND b.endDate > :#{#criteria.startDate})
              AND NOT EXISTS (SELECT 1 FROM Rental rt WHERE rt.room = r
                              AND rt.startDate < :#{#criteria.endDate} AND rt.endDate > :#{#criteria.startDate})
            ORDER BY r.price, h.name, r.roomNumber
            """)
    List<Room> findAvailableRooms(@Param("criteria") RoomSearchCriteriaDTO criteria);

    /** Loads the room with a row lock so that concurrent bookings of the same room are serialized. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Room r WHERE r.idRoom = :id")
    Optional<Room> findByIdForUpdate(@Param("id") Integer id);

    @Query("SELECT r FROM Room r WHERE r.hotel.idHotel = :hotelId ORDER BY r.roomNumber")
    List<Room> findByHotelId(@Param("hotelId") int hotelId);
}
