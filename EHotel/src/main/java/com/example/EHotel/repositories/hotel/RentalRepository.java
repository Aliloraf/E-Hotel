package com.example.EHotel.repositories.hotel;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotel.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer>{

    <Optional> Rental findById(int id);
    List<Rental> findByCustomerSinCustomer(String sinCustomer);
}
