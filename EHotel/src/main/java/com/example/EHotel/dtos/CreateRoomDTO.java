package com.example.EHotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomDTO {
    
    private Integer roomNumber;
    private Boolean availability;
    private Double price;
    private String view;
    private Boolean extensible;
    private String capacity;
    private Integer idHotel;
}
