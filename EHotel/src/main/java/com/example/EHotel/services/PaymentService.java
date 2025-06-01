package com.example.EHotel.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import com.example.EHotel.model.hotel.Payment;
import com.example.EHotel.repositories.hotel.PaymentRepository;

@Service
@Transactional
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;

    @SuppressWarnings("null")
    public void addPayment(Payment payment) {
        paymentRepository.save(payment);

    }

    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }
}
