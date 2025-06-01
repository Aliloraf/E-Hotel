package com.example.EHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.EHotel.dtos.AddPaymentDTO;
import com.example.EHotel.model.hotel.Payment;
import com.example.EHotel.services.PaymentService;
import com.example.EHotel.services.RentalService;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RentalService rentalService;

    @GetMapping("/payments")
    public String showPayments(Model model) {
        model.addAttribute("payments", paymentService.getPayments());
        return "payments";
    }

    @GetMapping("/add/{id}")
    public String showAddPaymentForm(@PathVariable("id") int idRental,  Model model) {
        AddPaymentDTO paymentDTO = new AddPaymentDTO();
        Integer id = idRental;
        paymentDTO.setIdRental(idRental);
        model.addAttribute("payment", paymentDTO);
        model.addAttribute("id", id);
        return "add-payment";
    }

    @PostMapping("/add/{id}")
    public String addPayment(@PathVariable("id") int idRental,  AddPaymentDTO paymentDTO) {
        Payment newPayment = new Payment();

        newPayment.setRental(rentalService.getRental(idRental));
        newPayment.setAmount(paymentDTO.getAmount());
        newPayment.setPaymentDate(paymentDTO.getPaymentDate());
        newPayment.setPaymentMethod(paymentDTO.getPaymentMethod());
        newPayment.setPaymentStatus(paymentDTO.getPaymentStatus());

        paymentService.addPayment(newPayment);

        return "redirect:/payment/payments";
    }
}
