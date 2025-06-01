package com.example.EHotel.model.hotel;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "booking_archieve")
public class BookingArchive {

    @Id
    @Column(name = "id_booking")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idBooking;

    @Column(name = "sin_customer", nullable = false)
    private String sinCustomer;

    @Column(name = "id_room", nullable = false)
    private Integer idRoom;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

}
