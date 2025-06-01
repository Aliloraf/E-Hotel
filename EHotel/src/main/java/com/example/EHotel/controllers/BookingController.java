package com.example.EHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.model.hotel.Rental;
import com.example.EHotel.services.BookingService;
import com.example.EHotel.services.RentalService;

@Controller
@RequestMapping("/booking")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RentalService rentalService;
    
    @GetMapping("/bookings")
    public String showBookings(Model model) {
        model.addAttribute("bookings", bookingService.getBookings());
        return "bookings";
    }

    @GetMapping("/transform/{id}")
    public String transformBooking(@PathVariable("id") int id, Model model) {
        Booking booking = bookingService.getBooking(id);
        Rental rental = new Rental();
        rental.setCustomer(booking.getCustomer());
        rental.setRoom(booking.getRoom());
        rental.setStartDate(booking.getStartDate());
        rental.setEndDate(booking.getEndDate());

        rentalService.addRental(rental);
        return "redirect:/booking/bookings";
    }

    @GetMapping("/rentals")
    public String showRentals(Model model) {
        model.addAttribute("rentals", rentalService.getRentals());
        return "rentals";
    }


}
