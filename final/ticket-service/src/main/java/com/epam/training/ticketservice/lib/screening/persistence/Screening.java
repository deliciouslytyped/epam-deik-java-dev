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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "screening",
        uniqueConstraints = {
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
    public Long screeningId;

    @Column(nullable = false)
    public BaseScreening screening;
}
