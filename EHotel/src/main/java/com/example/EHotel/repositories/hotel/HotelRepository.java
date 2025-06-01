package com.example.EHotel.repositories.hotel;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotel.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer>{
    Optional<Hotel> findById(int id);
}
