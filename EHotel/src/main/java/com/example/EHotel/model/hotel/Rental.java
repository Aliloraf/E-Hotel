package com.example.EHotel.model.hotel;

import java.time.LocalDate;

import com.example.EHotel.model.customer.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "rental")
public class Rental {

    @Id
    @Column(name = "id_rental")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRental;

    @ManyToOne
    @JoinColumn(name = "sin_customer", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
