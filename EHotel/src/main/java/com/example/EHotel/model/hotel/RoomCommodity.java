package com.example.EHotel.model.hotel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Table(name = "room_commodity")
@IdClass(RoomCommodityId.class)
public class RoomCommodity {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_room")
    private Room room;

    @Id
    @ManyToOne
    @JoinColumn(name = "commodity_name")
    private Commodity commodity;
}
