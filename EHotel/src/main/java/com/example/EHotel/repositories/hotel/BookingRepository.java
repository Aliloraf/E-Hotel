package com.example.EHotel.repositories.hotel;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotel.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>{
    
    Optional<Booking> findById(int id);
    List<Booking> findByCustomerSinCustomer(String sinCustomer);
}
