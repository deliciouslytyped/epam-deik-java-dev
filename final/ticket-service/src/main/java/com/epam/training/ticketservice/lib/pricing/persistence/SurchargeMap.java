package com.epam.training.ticketservice.lib.pricing.persistence;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SurchargeMap {
    // TODO https://stackoverflow.com/questions/1032486/what-is-the-proper-jpa-mapping-for-id-in-parent-and-unique-sequence-in-base-cla
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;

    @ManyToOne
    public Surcharge surcharge;
}
