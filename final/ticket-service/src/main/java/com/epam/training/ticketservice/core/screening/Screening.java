package com.epam.training.ticketservice.core.screening;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Screening {

    @Id
    @GeneratedValue
    private Integer id;
    private String movieName;
    private String roomName;
    private Date date;

    public Screening(String movieName, String roomName, Date date) {
        this.movieName = movieName;
        this.roomName = roomName;
        this.date = date;
    }
}
