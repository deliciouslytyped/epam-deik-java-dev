package com.epam.training.ticketservice.lib.screening.persistence;

import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Embeddable
@Entity
@Table(name = "screening")
public class BaseScreening {

    @ManyToOne
    public Room room;

    @ManyToOne
    public Movie movie;

    public Instant startTime;

    @Column(nullable = false)
    public Long screeningId;
}
