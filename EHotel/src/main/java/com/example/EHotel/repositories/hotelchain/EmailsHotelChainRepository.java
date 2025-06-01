package com.example.EHotel.repositories.hotelchain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotelchain.EmailsHotelChain;

@Repository
public interface EmailsHotelChainRepository extends JpaRepository<EmailsHotelChain, String>{

}
