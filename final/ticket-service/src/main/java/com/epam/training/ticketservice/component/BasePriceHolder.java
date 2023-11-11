package com.epam.training.ticketservice.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class BasePriceHolder {
    private int basePrice = 1500;
}
