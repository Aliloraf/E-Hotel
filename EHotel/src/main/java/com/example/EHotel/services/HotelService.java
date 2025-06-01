package com.example.EHotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EHotel.repositories.hotel.HotelRepository;
import com.example.EHotel.model.hotel.Hotel;
import java.util.List;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getHotels() {
        return hotelRepository.findAll();
    }

    public Hotel getHotel(int id) {
        return hotelRepository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public void addHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public void deleteHotel(int id) {
        hotelRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public Integer findUnusedId() {
        List<Hotel> hotels = hotelRepository.findAll();
        int expectedId =  hotels.get(hotels.size() - 1).getIdHotel() + 1;
        return expectedId;
    }
}
