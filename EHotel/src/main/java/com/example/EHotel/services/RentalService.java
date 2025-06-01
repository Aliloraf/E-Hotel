package com.example.EHotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EHotel.model.hotel.Rental;
import com.example.EHotel.repositories.hotel.RentalRepository;
import java.util.List;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RentalService {
    
    @Autowired
    private RentalRepository rentalRepository;

    @SuppressWarnings("null")
    public void addRental(Rental rental) {
        rentalRepository.save(rental);
    }
    
    public Rental getRental(int id) {
        return rentalRepository.findById(id);
    }

    public void deleteRental(int id) {
        rentalRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    public void updateRental(Rental rental) {
        rentalRepository.save(rental);
    }

    public Integer findUnusedId() {
        return rentalRepository.findAll().get(rentalRepository.findAll().size() - 1).getIdRental() + 1;
    }

    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }
}
