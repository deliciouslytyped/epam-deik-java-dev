package com.epam.training.ticketservice.lib.screening.persistence;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.UniqueConstraint;

// The leaky abstraction is quite visible here
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "screening",
        uniqueConstraints = {
                // The naming is kind of funky; the columns joined/used from other tables use the database
                // naming strategy but the columns used from this entity use the java-side names
                @UniqueConstraint(name = "alternate_pk", columnNames = {"MOVIE_TITLE", "startTime", "ROOM_NAME"})
        })
public class Screening {

    /**
     * This is a bijection to the screening key, it's a (Terminology?) derived / alternate / surrogate key.
     * Per the ER design the key is actually the composite key, but we use screeningId instead, so the PK
     * constraints (nonnull, uniqueness) need to be set on the BaseScreening relationship.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long screeningId; // We consider this a database internal field and it isnt exposed paste the data access layer; i.e. not in the DTO

    //TODO it would probably simplify things a lot to make this the primary key instead, but I guess doing this might make the code more flexible down the line? (but at what cost)
    @Column(nullable = false) //TODO does column even make sense here? This is an embeded...?
    public BaseScreening screening;
}