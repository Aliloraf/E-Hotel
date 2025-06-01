package com.example.EHotel.model.employee;

import com.example.EHotel.model.hotel.Hotel;

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
@Table(name = "employee")
public class Employee {
    @Id
    @Column(name = "sin_employee")
    private String sinEmployee;
    
    @Column(name = "firstname", nullable = false)
    private String firstname;
    
    @Column(name = "lastname", nullable = false)
    private String lastname;
    
    @Column(name = "role")
    private String role;
    
    @Column(name = "street_number", nullable = false)
    private Integer streetNumber;
    
    @Column(name = "street_name", nullable = false)
    private String streetName;
    
    @Column(name = "city", nullable = false)
    private String city;
    
    @Column(name = "postal_code", nullable = false)
    private String postalCode;
    
    @Column(name = "country", nullable = false)
    private String country;
    
    @ManyToOne
    @JoinColumn(name = "id_hotel")
    private Hotel hotel;

}
