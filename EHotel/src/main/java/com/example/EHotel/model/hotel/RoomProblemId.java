package com.example.EHotel.model.hotel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** Composite primary key of {@link RoomProblem}. */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoomProblemId implements Serializable {
    private Integer room;
    private Integer problem;
}
