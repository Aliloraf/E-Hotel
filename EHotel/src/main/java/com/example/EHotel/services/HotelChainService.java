package com.example.EHotel.services;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EHotel.exceptions.ResourceNotFoundException;
import com.example.EHotel.model.hotelchain.HotelChain;
import com.example.EHotel.repositories.hotelchain.HotelChainRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HotelChainService {

    private final HotelChainRepository hotelChainRepository;

    public List<HotelChain> getHotelChains() {
        return hotelChainRepository.findAll(Sort.by("name"));
    }

    public HotelChain getHotelChain(int id) {
        return hotelChainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chaîne hôtelière introuvable : " + id));
    }
}
