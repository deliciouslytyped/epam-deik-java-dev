package com.epam.training.ticketservice.lib.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link com.epam.training.ticketservice.lib.reservation.persistence.Seat}
 */
@Data
public class SeatDto implements Serializable {
    private final Integer rowIdx;
    private final Integer colIdx;
}