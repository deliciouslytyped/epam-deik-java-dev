package com.epam.training.ticketservice.lib.pricing.persistence;

import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

// TODO figure out what the correct setting is here
//  https://stackoverflow.com/questions/38572566/warning-equals-hashcode-on-lomboks-data-annotation-with-inheritance
@EqualsAndHashCode(callSuper = true)
@Data
public class ScreeningSurchargeMap extends SurchargeMap {
    @ManyToOne
    public Screening screening;
}
