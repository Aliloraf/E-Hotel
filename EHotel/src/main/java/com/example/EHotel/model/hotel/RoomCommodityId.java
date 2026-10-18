package com.example.EHotel.model.hotel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** Composite primary key of {@link RoomCommodity}. */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoomCommodityId implements Serializable {
    private Integer room;
    private String commodity;
}
