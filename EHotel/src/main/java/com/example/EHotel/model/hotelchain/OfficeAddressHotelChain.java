package com.example.EHotel.model.hotelchain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "office_address_hotel_chain")
public class OfficeAddressHotelChain {


    @Id
    private Integer id;
    
    @Column(name = "street_number", nullable = false)
    private Integer streetNumber;
    
    @Column(name = "street_name", nullable = false)
    private String streetName;
    
    @Column(name = "city", nullable = false)
    private String city;
    
    @Column(name = "postal_code", unique = true, nullable = false)
    private String postalCode;
    
    @Column(name = "country", nullable = false)
    private String country;
    
    @ManyToOne
    @JoinColumn(name = "id_hotel_chain", nullable = false)
    private HotelChain hotelChain;
}
