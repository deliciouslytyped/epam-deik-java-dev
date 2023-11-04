package com.epam.training.ticketservice.core.screening;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Screening {

    @Id
    @GeneratedValue
    private Integer id;
    private String movieName;
    private String roomName;
    private String date;

    public Screening(String movieName, String roomName, String date) {
        this.movieName = movieName;
        this.roomName = roomName;
        this.date = date;
    }
}
