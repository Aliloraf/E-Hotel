package com.example.EHotel.dtos;

import static com.example.EHotel.dtos.ValidationPatterns.*;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Booking form. Only the SIN is required for a returning customer; the name and address
 * fields are required when the SIN is unknown (checked in BookingService).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    @NotBlank(message = "Le NAS est obligatoire")
    @Pattern(regexp = SIN, message = SIN_MESSAGE)
    private String sinCustomer;

    @Size(max = 255)
    private String firstname;

    @Size(max = 255)
    private String lastname;

    @Positive(message = "Le numéro de rue doit être positif")
    private Integer streetNumber;

    @Size(max = 255)
    private String streetName;

    @Size(max = 255)
    private String city;

    @Pattern(regexp = POSTAL_CODE, message = POSTAL_CODE_MESSAGE)
    private String postalCode;

    @Pattern(regexp = COUNTRY, message = COUNTRY_MESSAGE)
    private String country;

    @NotNull
    private Integer idRoom;

    @NotNull(message = "La date d'arrivée est obligatoire")
    @FutureOrPresent(message = "La date d'arrivée ne peut pas être dans le passé")
    private LocalDate startDate;

    @NotNull(message = "La date de départ est obligatoire")
    private LocalDate endDate;

    @AssertTrue(message = "La date de départ doit être après la date d'arrivée")
    public boolean isEndDateAfterStartDate() {
        return startDate == null || endDate == null || endDate.isAfter(startDate);
    }

    public boolean hasNewCustomerDetails() {
        return firstname != null && lastname != null && streetNumber != null && streetName != null
                && city != null && postalCode != null && country != null;
    }
}
