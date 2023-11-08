package com.epam.training.ticketservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "rooms")
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int rows;
    private int columns;

    @OneToMany(mappedBy = "room")
    private List<Screening> screenings;

    @OneToOne
    @JoinColumn(name = "component_id", referencedColumnName = "id")
    private PriceComponent priceComponent;

    public Room(String name, int rows, int columns) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }
}
