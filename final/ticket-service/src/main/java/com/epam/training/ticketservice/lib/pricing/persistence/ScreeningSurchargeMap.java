package com.epam.training.ticketservice.lib.pricing.persistence;

import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.pricing.persistence.base.SurchargeMap;
import com.epam.training.ticketservice.lib.screening.persistence.Screening;
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
public class ScreeningSurchargeMap extends SurchargeMap {
    @ManyToOne
    public Screening screening;

    public ScreeningSurchargeMap(Long id, Surcharge s, Screening sc){
        super(id, s);
        this.screening = sc;
    }
}
