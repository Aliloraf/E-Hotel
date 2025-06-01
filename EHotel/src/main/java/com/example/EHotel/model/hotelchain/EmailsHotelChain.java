package com.example.EHotel.model.hotelchain;

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
@Table(name = "emails_hotel_chain")
public class EmailsHotelChain {
    @Id
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "id_hotel_chain", nullable = false)
    private HotelChain hotelChain;
}
