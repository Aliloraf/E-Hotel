package com.example.EHotel.model.hotelchain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel_chain")
public class HotelChain {
    @Id
    @Column(name = "id_hotel_chain")
    private Integer id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(name = "hotels_number")
    private int numberOfHotels;

}
