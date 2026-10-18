package com.example.EHotel.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "La date est obligatoire")
    @FutureOrPresent(message = "La date du paiement ne peut pas être dans le passé")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate paymentDate = LocalDate.now();

    @NotBlank(message = "La méthode de paiement est obligatoire")
    private String paymentMethod;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être positif")
    @Digits(integer = 6, fraction = 2, message = "Montant invalide (max. 999999.99)")
    private BigDecimal amount;

    @NotBlank(message = "Le statut est obligatoire")
    private String paymentStatus;
}
