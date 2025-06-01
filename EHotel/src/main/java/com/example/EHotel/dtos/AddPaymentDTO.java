package com.example.EHotel.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddPaymentDTO {
    
    private Integer idRental;
    private LocalDate paymentDate;
    private String paymentMethod;
    private Double amount;
    private String paymentStatus;
}
