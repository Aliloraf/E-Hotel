package com.example.EHotel.repositories.hotel;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotel.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>{

    List<Booking> findByCustomerSinCustomer(String sinCustomer);

    List<Booking> findAllByOrderByStartDateAsc();

    /** True if the room already has a booking whose stay overlaps [startDate, endDate). */
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.room.idRoom = :roomId AND b.startDate < :endDate AND b.endDate > :startDate")
    boolean existsOverlapping(@Param("roomId") Integer roomId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
