package com.epam.training.ticketservice.core.movie;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Integer id;
    private String movieName;
    private String genre;
    private int length;

    public Movie(String movieName, String genre, int length) {
        this.movieName = movieName;
        this.genre = genre;
        this.length = length;
    }
}
