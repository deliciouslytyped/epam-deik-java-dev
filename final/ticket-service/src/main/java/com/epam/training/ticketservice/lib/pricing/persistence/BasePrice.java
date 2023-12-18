package com.epam.training.ticketservice.lib.pricing.persistence;

import com.epam.training.ticketservice.support.db.base.SingletonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * This stores a singleton entity value for the base price.
 */
// TODO figure out what the correct setting is here
//  https://stackoverflow.com/questions/38572566/warning-equals-hashcode-on-lomboks-data-annotation-with-inheritance
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity //TODO does this need the annotation since the parent class is a SINGLE_TABLE?
@DiscriminatorValue("BasePrice")
public class BasePrice extends SingletonEntity {
    @Column(nullable = false)
    //public Currency basePrice;
    public Integer basePrice;

    public BasePrice(Integer basePrice) {
        super("BasePrice");
        this.basePrice = basePrice;
    }
}
