package com.epam.training.ticketservice.component;

import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.model.Screening;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PriceCalculator {

    public int calculate(Screening screening, int basePrice, int ticketAmount) {
        int movieComp = Optional.ofNullable(screening.getMovie().getPriceComponent())
                .map(PriceComponent::getAmount).orElse(0);
        int roomComp = Optional.ofNullable(screening.getRoom().getPriceComponent())
                .map(PriceComponent::getAmount).orElse(0);
        int scrComp = Optional.ofNullable(screening.getPriceComponent())
                .map(PriceComponent::getAmount).orElse(0);

        return (basePrice + movieComp + roomComp + scrComp) * ticketAmount;
    }
}
