package com.example.EHotel.model.hotel;

import com.example.EHotel.model.employee.Employee;
import com.example.EHotel.model.hotelchain.HotelChain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotel")
public class Hotel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hotel")
    private Integer idHotel;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "rooms_number", nullable = false)
    private Integer roomsNumber;
    
    @Column(name = "start_number", nullable = false)
    private Integer startNumber;
    
    @Column(name = "email", nullable = false)
    private String email;
    
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

    @OneToOne
    @JoinColumn(name = "sin_manager")
    private Employee manager;
}
