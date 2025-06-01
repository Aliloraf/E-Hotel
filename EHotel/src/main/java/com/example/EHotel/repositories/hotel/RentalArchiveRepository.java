package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.hotel.RentalArchive;

@Repository
public interface RentalArchiveRepository extends JpaRepository<RentalArchive, Integer>{

}
