package com.example.EHotel.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomSearchCriteriaDTO {

    private String roomCapacity;
    private double maxPrice;
    private int hotelChainId;
    private int startNumber;
    private int roomsNumber;
    private LocalDate startDate;
    private LocalDate endDate;
}
