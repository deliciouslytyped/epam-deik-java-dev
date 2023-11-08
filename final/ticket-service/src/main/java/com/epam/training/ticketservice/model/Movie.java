package com.epam.training.ticketservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "movies")
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String category;
    private int length;

    @OneToMany(mappedBy = "movie")
    private List<Screening> screenings;

    @OneToOne
    @JoinColumn(name = "component_id", referencedColumnName = "id")
    private PriceComponent priceComponent;

    public Movie(String title, String category, int length) {
        this.title = title;
        this.category = category;
        this.length = length;
    }
}
