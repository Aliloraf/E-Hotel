package com.example.EHotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EHotel.repositories.hotelchain.HotelChainRepository;

import jakarta.transaction.Transactional;
import com.example.EHotel.model.hotelchain.HotelChain;
import java.util.List;

@Service
@Transactional
public class HotelChainService {
    
    @Autowired
    private HotelChainRepository hotelChainRepository;

    public List<HotelChain> getHotelChains() {
        return hotelChainRepository.findAll();
    }

    public HotelChain getHotelChain(int id) {
        return hotelChainRepository.findById(id).orElse(null);
    }
}
