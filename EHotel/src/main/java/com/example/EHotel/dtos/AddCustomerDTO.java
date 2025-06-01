
package com.example.EHotel.dtos;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerDTO {
    private String sinCustomer;
    private String firstname;
    private String lastname;
    private LocalDate checkInDate = LocalDate.now();
    private Integer streetNumber;
    private String streetName; 
    private String city;
    private String postalCode;
    private String country;
}
