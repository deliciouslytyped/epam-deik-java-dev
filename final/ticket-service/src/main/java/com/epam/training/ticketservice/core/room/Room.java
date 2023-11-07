package com.epam.training.ticketservice.core.room;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private int rows;
    private int cols;

    public Room(String name, int rows, int cols) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
    }
}
