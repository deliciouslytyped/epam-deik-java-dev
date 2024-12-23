package com.epam.training.ticketservice.lib.pricing.persistence.model;

import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;
import lombok.Data;

@Data
public class TicketPriceDto {
    public final ScreeningDto screening;
    public final Integer seatCount;
}
