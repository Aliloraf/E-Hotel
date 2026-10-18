package com.example.EHotel.dtos;

import static com.example.EHotel.dtos.ValidationPatterns.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerDTO {

    @NotBlank(message = "Le NAS est obligatoire")
    @Pattern(regexp = SIN, message = SIN_MESSAGE)
    private String sinCustomer;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 255)
    private String firstname;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 255)
    private String lastname;

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
}
