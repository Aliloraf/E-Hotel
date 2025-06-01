package com.example.EHotel.repositories.hotelchain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotelchain.HotelChain;
import java.util.Optional;

@Repository
public interface HotelChainRepository extends JpaRepository<HotelChain, Integer>{

    Optional<HotelChain> findById(int id);

}
