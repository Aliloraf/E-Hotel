package com.example.EHotel.services;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EHotel.dtos.AddPaymentDTO;
import com.example.EHotel.model.hotel.Payment;
import com.example.EHotel.repositories.hotel.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalService rentalService;

    public Payment addPayment(int rentalId, AddPaymentDTO dto) {
        Payment payment = new Payment();
        payment.setRental(rentalService.getRental(rentalId));
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus());
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public List<Payment> getPayments() {
        return paymentRepository.findAll(Sort.by(Sort.Direction.DESC, "paymentDate"));
    }
}
