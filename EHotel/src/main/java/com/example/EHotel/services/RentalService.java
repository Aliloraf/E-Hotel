package com.example.EHotel.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EHotel.exceptions.ResourceNotFoundException;
import com.example.EHotel.model.hotel.Rental;
import com.example.EHotel.repositories.hotel.RentalRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;

    public List<Rental> getRentals() {
        return rentalRepository.findAllByOrderByStartDateAsc();
    }

    public Rental getRental(int id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location introuvable : " + id));
    }
}
