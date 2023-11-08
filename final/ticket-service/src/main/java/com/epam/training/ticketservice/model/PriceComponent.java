package com.epam.training.ticketservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "price_components")
@NoArgsConstructor
public class PriceComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int amount;

    public PriceComponent(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
