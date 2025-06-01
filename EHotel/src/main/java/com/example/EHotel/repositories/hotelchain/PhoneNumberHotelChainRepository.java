package com.example.EHotel.repositories.hotelchain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotelchain.PhoneNumberHotelChain;

@Repository
public interface PhoneNumberHotelChainRepository extends JpaRepository<PhoneNumberHotelChain, String>{

}
