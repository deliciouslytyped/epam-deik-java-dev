package com.epam.training.ticketservice.lib.screening.persistence;


import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.support.jparepo.CheckConstraint;
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
@Entity //TODO I messed this up, should be keyed by just room and time, movie is a funcitonal dependency
@Table(
        name = "screening",
        uniqueConstraints = {
                // The naming is kind of funky; the columns joined/used from other tables use the database
                // naming strategy but the columns used from this entity use the java-side names
                @UniqueConstraint(name = "alternate_pk", columnNames = {"MOVIE_TITLE", "startTime", "ROOM_NAME"})
        })
@CheckConstraint(name="FUTURE_ONLY", check="CURRENT_TIMESTAMP < START_TIME")
//TODO wrap these in not exists
//The syntax is a little funny here, the inside of subqueries shadows the outside so we need to use
// the table name to refer to the outer columns in the check constraint. TODO Would be nice to have
// some reference to link that explains this.

//TODO these check constraints refer to other tables which breaks dropping because hibernate doesnt know about them
// 1) is there a way to set up the sql so taht cascading will also deal with dropping constraints?
// 2) is there a way to tell hibernate about the dependency?
@CheckConstraint(driverFilter="org.hibernate.dialect.H2Dialect", name="NO_OVERLAP", alsoDependsOn={Movie.class}, check= "\n" +
        "    NOT EXISTS ( SELECT 1 \n" +
        "      FROM SCREENING s\n" +
        "      JOIN MOVIE m\n" +
        "        ON s.MOVIE_TITLE = m.TITLE\n" +
        "      JOIN MOVIE my\n" +
        "        ON SCREENING.MOVIE_TITLE = my.TITLE\n" +
        "  " +
        "      WHERE SCREENING.ROOM_NAME = s.ROOM_NAME AND SCREENING.SCREENING_ID != s.SCREENING_ID\n" +
        "        -- There are three cases; left no overlap, overlap, right no overlap\n" +
        "        -- If we end before they start, or they end before we start, we're OK.\n" +
        "        AND NOT ((DATEADD(MINUTE, my.RUNTIME, SCREENING.START_TIME) <= s.START_TIME) \n" +
        "             OR  (DATEADD(MINUTE, m.RUNTIME, s.START_TIME) <= SCREENING.START_TIME))\n" +
        "    )")
@CheckConstraint(driverFilter="org.hibernate.dialect.H2Dialect", name="NO_OVERLAP_BREAK", alsoDependsOn={Movie.class}, check= "\n" +
        "    NOT EXISTS ( SELECT 1 \n" +
        "      FROM SCREENING s\n" +
        "      JOIN MOVIE m\n" +
        "        ON s.MOVIE_TITLE = m.TITLE\n" +
        "      JOIN MOVIE my\n" +
        "        ON SCREENING.MOVIE_TITLE = my.TITLE\n" +
        "  " +
        "      WHERE ROOM_NAME = s.ROOM_NAME AND SCREENING.SCREENING_ID != s.SCREENING_ID\n" +
        "        -- There are three cases; left no overlap, overlap, right no overlap\n" +
        "        -- If we end before they start, or they end before we start, we're OK.\n" +
        "        AND NOT ((DATEADD(MINUTE, my.RUNTIME + 10, SCREENING.START_TIME) <= s.START_TIME) \n" +
        "             OR  (DATEADD(MINUTE, m.RUNTIME + 10, s.START_TIME) <= SCREENING.START_TIME))\n" +
        "    )")
//Postgres doesnt support check constraints and we're gonna need to do some additional design work
//@CheckConstraint(driverFilter= "org.hibernate.dialect.PostgreSQLDialect", name="no_overlap", check="")
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