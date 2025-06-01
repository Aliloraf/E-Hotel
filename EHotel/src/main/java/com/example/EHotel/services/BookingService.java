package com.example.EHotel.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.repositories.hotel.BookingRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @SuppressWarnings("null")
    public void addBooking(Booking booking) {
        bookingRepository.save(booking);

    }

    public Booking getBooking(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> getBookings(String sinCustomer) {
        return bookingRepository.findByCustomerSinCustomer(sinCustomer);
    }

    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    public void deleteBooking(int id) {
        bookingRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    public void updateBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public Integer findUnusedId() {
        List<Booking> bookings = bookingRepository.findAll();
        int expectedId = bookings.get(bookings.size() - 1).getIdBooking() + 1;
        return expectedId;
    }

}
