package com.example.EHotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddEmployeeDTO {

    private String sinEmployee;
    private String firstname;
    private String lastname;
    private String role;
    private Integer streetNumber;
    private String streetName;
    private String city;
    private String postalCode;
    private String country;
    private Integer idHotel;
}
