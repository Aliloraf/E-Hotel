package com.example.EHotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.EHotel.dtos.AddPaymentDTO;
import com.example.EHotel.services.PaymentService;
import com.example.EHotel.services.RentalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final RentalService rentalService;

    @GetMapping("/payments")
    public String showPayments(Model model) {
        model.addAttribute("payments", paymentService.getPayments());
        return "payments";
    }

    @GetMapping("/add/{id}")
    public String showAddPaymentForm(@PathVariable("id") int idRental, Model model) {
        AddPaymentDTO paymentDTO = new AddPaymentDTO();
        paymentDTO.setIdRental(idRental);
        model.addAttribute("payment", paymentDTO);
        model.addAttribute("rental", rentalService.getRental(idRental));
        return "add-payment";
    }

    @PostMapping("/add/{id}")
    public String addPayment(@PathVariable("id") int idRental,
            @Valid @ModelAttribute("payment") AddPaymentDTO paymentDTO, BindingResult result,
            Model model, RedirectAttributes redirect) {
        paymentDTO.setIdRental(idRental);
        if (result.hasErrors()) {
            model.addAttribute("rental", rentalService.getRental(idRental));
            return "add-payment";
        }
        paymentService.addPayment(idRental, paymentDTO);
        redirect.addFlashAttribute("success", "Paiement enregistré.");
        return "redirect:/payment/payments";
    }
}
