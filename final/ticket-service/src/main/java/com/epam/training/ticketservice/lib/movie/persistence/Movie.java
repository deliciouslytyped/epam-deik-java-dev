package com.epam.training.ticketservice.lib.movie.persistence;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    public String title;

    @Column(nullable = false)
    public String genre;

    /**
     * Runtime in minutes.
     */
    @Column(nullable = false)
    public Integer runtime;
}
