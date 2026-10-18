package com.example.EHotel.model.customer;

import java.time.LocalDate;

import com.example.EHotel.dtos.AddCustomerDTO;

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
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "sin_customer")
    private String sinCustomer;
    
    @Column(name = "firstname", nullable = false)
    private String firstname;
    
    @Column(name = "lastname", nullable = false)
    private String lastname;
    
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;
    
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

    public Customer(AddCustomerDTO customer) {
        this.sinCustomer = customer.getSinCustomer();
        this.firstname = customer.getFirstname();
        this.lastname = customer.getLastname();
        this.streetNumber = customer.getStreetNumber();
        this.streetName = customer.getStreetName();
        this.city = customer.getCity();
        this.postalCode = customer.getPostalCode();
        this.country = customer.getCountry();
    }
}
