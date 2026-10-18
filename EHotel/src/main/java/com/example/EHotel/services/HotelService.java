package com.example.EHotel.services;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EHotel.dtos.CreateHotelDTO;
import com.example.EHotel.exceptions.ResourceNotFoundException;
import com.example.EHotel.model.hotel.Hotel;
import com.example.EHotel.repositories.employee.EmployeeRepository;
import com.example.EHotel.repositories.hotel.HotelRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelChainService hotelChainService;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<Hotel> getHotels() {
        return hotelRepository.findAll(Sort.by("idHotel"));
    }

    @Transactional(readOnly = true)
    public Hotel getHotel(int id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hôtel introuvable : " + id));
    }

    @Transactional(readOnly = true)
    public List<String> getCities() {
        return hotelRepository.findDistinctCities();
    }

    public Hotel addHotel(CreateHotelDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setRoomsNumber(0); // incremented by a database trigger for every room added
        copyFields(dto, hotel);
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(int id, CreateHotelDTO dto) {
        Hotel hotel = getHotel(id);
        copyFields(dto, hotel);
        return hotelRepository.save(hotel);
    }

    /** Deleting a hotel also deletes its rooms, phone numbers, bookings and rentals (ON DELETE CASCADE). */
    public void deleteHotel(int id) {
        hotelRepository.delete(getHotel(id));
        hotelRepository.flush();
    }

    public CreateHotelDTO toDto(Hotel hotel) {
        return new CreateHotelDTO(hotel.getIdHotel(), hotel.getName(), hotel.getStartNumber(), hotel.getEmail(),
                hotel.getStreetNumber(), hotel.getStreetName(), hotel.getCity(), hotel.getPostalCode(),
                hotel.getCountry(), hotel.getHotelChain().getId(),
                hotel.getManager() != null ? hotel.getManager().getSinEmployee() : null);
    }

    private void copyFields(CreateHotelDTO dto, Hotel hotel) {
        hotel.setName(dto.getName());
        hotel.setStartNumber(dto.getStartNumber());
        hotel.setEmail(dto.getEmail());
        hotel.setStreetNumber(dto.getStreetNumber());
        hotel.setStreetName(dto.getStreetName());
        hotel.setCity(dto.getCity());
        hotel.setPostalCode(dto.getPostalCode());
        hotel.setCountry(dto.getCountry());
        hotel.setHotelChain(hotelChainService.getHotelChain(dto.getIdHotelChain()));
        hotel.setManager(dto.getSinManager() == null ? null
                : employeeRepository.findById(dto.getSinManager()).orElseThrow(
                        () -> new ResourceNotFoundException("Employé introuvable : " + dto.getSinManager())));
    }
}
