package com.epam.training.ticketservice.lib.movie.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
