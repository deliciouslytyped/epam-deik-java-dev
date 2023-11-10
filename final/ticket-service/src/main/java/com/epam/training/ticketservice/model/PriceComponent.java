package com.epam.training.ticketservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


@Data
@Entity
@Table(name = "price_components")
@NoArgsConstructor
public class PriceComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int amount;

    @OneToMany(mappedBy = "priceComponent")
    private List<Movie> movies;
    @OneToMany(mappedBy = "priceComponent")
    private List<Room> rooms;
    @OneToMany(mappedBy = "priceComponent")
    private List<Screening> screenings;

    public PriceComponent(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
