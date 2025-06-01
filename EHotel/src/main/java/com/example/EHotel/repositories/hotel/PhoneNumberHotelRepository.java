package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotel.PhoneNumberHotel;

@Repository
public interface PhoneNumberHotelRepository extends JpaRepository<PhoneNumberHotel, String>{

}
