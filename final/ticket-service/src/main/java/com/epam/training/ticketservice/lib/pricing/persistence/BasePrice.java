package com.epam.training.ticketservice.lib.pricing.persistence;

import com.epam.training.ticketservice.lib.db.SingletonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Currency;

/**
 * This stores a singleton entity value for the base price.
 */
// TODO figure out what the correct setting is here
//  https://stackoverflow.com/questions/38572566/warning-equals-hashcode-on-lomboks-data-annotation-with-inheritance
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("BasePrice")
public class BasePrice extends SingletonEntity {
    @Column(nullable = false)
    public Currency basePrice;
}
