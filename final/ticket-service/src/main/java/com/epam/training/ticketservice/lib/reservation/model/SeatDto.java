package com.epam.training.ticketservice.lib.reservation.model;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.epam.training.ticketservice.lib.reservation.persistence.Seat}
 */
@Data
public class SeatDto implements Serializable {
    private final Integer rowIdx;
    private final Integer colIdx;

    public String toPairString() {
        return "(" + rowIdx.toString() + "," + colIdx.toString() + ")";
    }
}