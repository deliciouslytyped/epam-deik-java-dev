package com.epam.training.ticketservice.lib.screening.persistence;


import javax.persistence.*;

@Entity
@Table(
        name = "screening",
        uniqueConstraints = { @UniqueConstraint(columnNames = "baseScreening") })
public class Screening {

    /**
     * This is a bijection to the screening key, it's a (Terminology?) derived / alternate / surrogate key.
     * Per the ER design the key is actually the composite key, but we use screeningId instead, so the PK
     * constraints (nonnull, uniqueness) need to be set on the BaseScreening relationship.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long screeningId;

    @Column(name ="baseScreening", nullable = false)
    public BaseScreening screening;
}
