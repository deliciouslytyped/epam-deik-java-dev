package com.epam.training.ticketservice.lib.pricing.persistence;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Currency;

@Data
@Entity
@Table(name = "surcharge")
public class Surcharge {
    @Id
    public String name;

    @Column(nullable = false)
    public Currency value;
}
