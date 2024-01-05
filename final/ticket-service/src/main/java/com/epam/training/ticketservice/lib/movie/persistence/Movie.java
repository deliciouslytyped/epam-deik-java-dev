package com.epam.training.ticketservice.lib.movie.persistence;

import com.epam.training.ticketservice.support.jparepo.CheckConstraint;
import com.epam.training.ticketservice.support.jparepo.GenUpdateByEntityFragment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GenUpdateByEntityFragment
@Entity
@CheckConstraint(name="CHECK_RUN_TIME", check="RUNTIME > 0")
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