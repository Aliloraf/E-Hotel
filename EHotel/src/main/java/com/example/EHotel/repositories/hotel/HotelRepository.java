package com.example.EHotel.repositories.hotel;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.EHotel.model.employee.Employee;
import com.example.EHotel.model.hotel.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer>{

    @Query("SELECT DISTINCT h.city FROM Hotel h ORDER BY h.city")
    List<String> findDistinctCities();

    List<Hotel> findByManager(Employee manager);
}
