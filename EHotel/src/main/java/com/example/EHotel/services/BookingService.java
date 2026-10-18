package com.example.EHotel.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EHotel.dtos.BookingDTO;
import com.example.EHotel.exceptions.BusinessRuleException;
import com.example.EHotel.exceptions.ResourceNotFoundException;
import com.example.EHotel.model.customer.Customer;
import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.model.hotel.Rental;
import com.example.EHotel.model.hotel.Room;
import com.example.EHotel.repositories.customer.CustomerRepository;
import com.example.EHotel.repositories.hotel.BookingRepository;
import com.example.EHotel.repositories.hotel.RentalRepository;
import com.example.EHotel.repositories.hotel.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RentalRepository rentalRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<Booking> getBookings() {
        return bookingRepository.findAllByOrderByStartDateAsc();
    }

    @Transactional(readOnly = true)
    public Booking getBooking(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable : " + id));
    }

    /**
     * Books a room for a customer. A returning customer is identified by their SIN; an unknown SIN
     * creates a new customer from the form details.
     *
     * @throws BusinessRuleException if the room is unavailable, already booked or rented for
     *                               overlapping dates, or a new customer's details are incomplete
     */
    public Booking createBooking(BookingDTO dto) {
        Room room = roomRepository.findByIdForUpdate(dto.getIdRoom())
                .orElseThrow(() -> new ResourceNotFoundException("Chambre introuvable : " + dto.getIdRoom()));
        if (!Boolean.TRUE.equals(room.getAvailability())) {
            throw new BusinessRuleException("Cette chambre n'est pas disponible à la réservation.");
        }
        if (isOccupied(room, dto.getStartDate(), dto.getEndDate())) {
            throw new BusinessRuleException("Cette chambre est déjà réservée ou louée pour ces dates.");
        }
        Customer customer = customerRepository.findById(dto.getSinCustomer())
                .orElseGet(() -> customerRepository.save(newCustomer(dto)));

        return bookingRepository.save(new Booking(customer, room, dto.getStartDate(), dto.getEndDate()));
    }

    /**
     * Converts a booking into a rental when the customer checks in. The booking itself is removed;
     * a copy is kept in booking_archieve by a database trigger.
     */
    public Rental convertToRental(int bookingId) {
        Booking booking = getBooking(bookingId);
        Rental rental = new Rental();
        rental.setCustomer(booking.getCustomer());
        rental.setRoom(booking.getRoom());
        rental.setStartDate(booking.getStartDate());
        rental.setEndDate(booking.getEndDate());
        rental = rentalRepository.save(rental);
        bookingRepository.delete(booking);
        return rental;
    }

    private boolean isOccupied(Room room, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.existsOverlapping(room.getIdRoom(), startDate, endDate)
                || rentalRepository.existsOverlapping(room.getIdRoom(), startDate, endDate);
    }

    private Customer newCustomer(BookingDTO dto) {
        if (!dto.hasNewCustomerDetails()) {
            throw new BusinessRuleException(
                    "Aucun client avec ce NAS : veuillez remplir le nom, le prénom et l'adresse complète.");
        }
        return new Customer(dto.getSinCustomer(), dto.getFirstname(), dto.getLastname(), LocalDate.now(),
                dto.getStreetNumber(), dto.getStreetName(), dto.getCity(), dto.getPostalCode(), dto.getCountry());
    }
}
