package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotel.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{
    
    
}
