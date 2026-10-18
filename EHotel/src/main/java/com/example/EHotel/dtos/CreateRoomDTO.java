package com.example.EHotel.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Room form, used for both creating and updating a room. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomDTO {

    private Integer idRoom;

    @NotNull(message = "Le numéro de chambre est obligatoire")
    @PositiveOrZero(message = "Le numéro de chambre ne peut pas être négatif")
    private Integer roomNumber;

    @NotNull
    private Boolean availability = true;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être positif")
    @Digits(integer = 6, fraction = 2, message = "Prix invalide (max. 999999.99)")
    private BigDecimal price;

    @NotBlank(message = "La vue est obligatoire")
    private String view;

    @NotNull
    private Boolean extensible = false;

    @NotBlank(message = "La capacité est obligatoire")
    private String capacity;

    @NotNull(message = "L'hôtel est obligatoire")
    private Integer idHotel;
}
