package com.example.EHotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.EHotel.model.hotel.Rental;
import com.example.EHotel.services.BookingService;
import com.example.EHotel.services.RentalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final RentalService rentalService;

    @GetMapping("/bookings")
    public String showBookings(Model model) {
        model.addAttribute("bookings", bookingService.getBookings());
        return "bookings";
    }

    /** Check-in: turns a booking into a rental. */
    @PostMapping("/transform/{id}")
    public String transformBooking(@PathVariable("id") int id, RedirectAttributes redirect) {
        Rental rental = bookingService.convertToRental(id);
        redirect.addFlashAttribute("success",
                "Réservation " + id + " transformée en location n° " + rental.getIdRental() + ".");
        return "redirect:/booking/rentals";
    }

    @GetMapping("/rentals")
    public String showRentals(Model model) {
        model.addAttribute("rentals", rentalService.getRentals());
        return "rentals";
    }
}
