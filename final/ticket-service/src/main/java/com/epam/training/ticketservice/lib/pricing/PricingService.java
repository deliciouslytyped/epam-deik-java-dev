package com.epam.training.ticketservice.lib.pricing;

import com.epam.training.ticketservice.lib.pricing.persistence.model.TicketPriceDto;
import com.epam.training.ticketservice.lib.screening.model.ScreeningDto;

import java.util.Optional;

public interface PricingService {
    Integer getBasePrice();
    Integer getPrice(TicketPriceDto t);

    void updateBasePrice(Integer integer);

    void createPriceComponent(String name, Integer integer);

    void attachPriceComponentToRoom(String name, String room);

    void attachPriceComponentToMovie(String name, String movie);

    void attachPriceComponentToScreening(String name, ScreeningDto screening);
}
