package com.epam.training.ticketservice.lib.movie.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedQuery(name="Movie.update", query="UPDATE Movie m SET m.genre = :genre, m.runtime = :runtime WHERE m.title = :title")
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
