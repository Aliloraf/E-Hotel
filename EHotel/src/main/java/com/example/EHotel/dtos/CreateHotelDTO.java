package com.example.EHotel.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateHotelDTO {
    
    private Integer idHotel;

    private String name;

    private Integer roomsNumber;

    private Integer startNumber;
    
    private String email;
    
    private Integer streetNumber;
    
    private String streetName;
    
    private String city;
    
    private String postalCode;

    private String country;

    private Integer idHotelChain;

    private String sinManager;
}
