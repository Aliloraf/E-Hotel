package com.example.EHotel.repositories.hotel;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotel.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer>{

    List<Rental> findByCustomerSinCustomer(String sinCustomer);

    List<Rental> findAllByOrderByStartDateAsc();

    /** True if the room already has a rental whose stay overlaps [startDate, endDate). */
    @Query("SELECT COUNT(r) > 0 FROM Rental r WHERE r.room.idRoom = :roomId AND r.startDate < :endDate AND r.endDate > :startDate")
    boolean existsOverlapping(@Param("roomId") Integer roomId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
