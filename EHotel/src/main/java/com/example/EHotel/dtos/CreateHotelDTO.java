package com.example.EHotel.dtos;

import static com.example.EHotel.dtos.ValidationPatterns.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Hotel form. The number of rooms is not editable: database triggers keep it in sync with the room table. */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateHotelDTO {

    private Integer idHotel;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 255)
    private String name;

    @NotNull(message = "Le nombre d'étoiles est obligatoire")
    @Min(value = 1, message = "Entre 1 et 5 étoiles")
    @Max(value = 5, message = "Entre 1 et 5 étoiles")
    private Integer startNumber;

    @NotBlank(message = "Le courriel est obligatoire")
    @Pattern(regexp = EMAIL, message = EMAIL_MESSAGE)
    private String email;

    @NotNull(message = "Le numéro de rue est obligatoire")
    @Positive(message = "Le numéro de rue doit être positif")
    private Integer streetNumber;

    @NotBlank(message = "Le nom de rue est obligatoire")
    @Size(max = 255)
    private String streetName;

    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 255)
    private String city;

    @NotBlank(message = "Le code postal est obligatoire")
    @Pattern(regexp = POSTAL_CODE, message = POSTAL_CODE_MESSAGE)
    private String postalCode;

    @NotBlank(message = "Le pays est obligatoire")
    @Pattern(regexp = COUNTRY, message = COUNTRY_MESSAGE)
    private String country;

    @NotNull(message = "La chaîne hôtelière est obligatoire")
    private Integer idHotelChain;

    /** Optional: a hotel can exist without a manager. */
    private String sinManager;
}
