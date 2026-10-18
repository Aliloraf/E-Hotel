package com.example.EHotel.model.hotel;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private Integer idPayment;
    
    @ManyToOne
    @JoinColumn(name = "id_rental", nullable = false)
    private Rental rental;
    
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
    
    @Column(name = "amount", nullable = false, precision = 8, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;
    
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;
}
