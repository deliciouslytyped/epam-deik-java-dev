package com.epam.training.ticketservice.lib.screening.persistence;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
        name = "screening"
        //TODO how to figure out names? // uniqueConstraints = { @UniqueConstraint(columnNames = "baseScreening") }
        )
public class Screening {

    /**
     * This is a bijection to the screening key, it's a (Terminology?) derived / alternate / surrogate key.
     * Per the ER design the key is actually the composite key, but we use screeningId instead, so the PK
     * constraints (nonnull, uniqueness) need to be set on the BaseScreening relationship.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long screeningId;

    @Column(nullable = false)
    public BaseScreening screening;
}
