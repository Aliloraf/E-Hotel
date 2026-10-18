package com.example.EHotel.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Room search form. Only the dates are required; a null criterion means "any". */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomSearchCriteriaDTO {

    @NotNull(message = "La date d'arrivée est obligatoire")
    @FutureOrPresent(message = "La date d'arrivée ne peut pas être dans le passé")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "La date de départ est obligatoire")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private String roomCapacity;

    @Positive(message = "Le prix maximum doit être positif")
    private BigDecimal maxPrice;

    private Integer hotelChainId;

    private String city;

    /** Minimum hotel category (stars). */
    @Min(value = 1, message = "Entre 1 et 5 étoiles")
    @Max(value = 5, message = "Entre 1 et 5 étoiles")
    private Integer startNumber;

    /** Minimum number of rooms in the hotel. */
    @PositiveOrZero(message = "Le nombre de chambres ne peut pas être négatif")
    private Integer roomsNumber;

    @AssertTrue(message = "La date de départ doit être après la date d'arrivée")
    public boolean isEndDateAfterStartDate() {
        return startDate == null || endDate == null || endDate.isAfter(startDate);
    }
}
