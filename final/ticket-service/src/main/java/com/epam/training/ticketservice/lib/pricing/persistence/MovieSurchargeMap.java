package com.epam.training.ticketservice.lib.pricing.persistence;

import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.pricing.persistence.base.SurchargeMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

// TODO figure out what the correct setting is here
//  https://stackoverflow.com/questions/38572566/warning-equals-hashcode-on-lomboks-data-annotation-with-inheritance
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class MovieSurchargeMap extends SurchargeMap {
    @ManyToOne
    public Movie movie;

    //TODO make these supers generic so they dont depend on the exact super impl
    //TODO doesnt lombok have something for calling super?
    public MovieSurchargeMap(Long id, Surcharge s, Movie movie){
        super(id, s);
        this.movie = movie;
    }
}
