package com.epam.training.ticketservice.lib.pricing.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "surcharge")
public class Surcharge {
    @Id
    public String name;

    @Column(nullable = false)
    //public Currency price;
    public Integer price;
}
