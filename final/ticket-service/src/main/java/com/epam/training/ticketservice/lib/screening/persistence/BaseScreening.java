package com.epam.training.ticketservice.lib.screening.persistence;

import com.epam.training.ticketservice.lib.movie.persistence.Movie;
import com.epam.training.ticketservice.lib.room.persistence.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BaseScreening implements Serializable {

    @OneToOne
    public Room room;

    @OneToOne
    public Movie movie;

    public Instant startTime;
}
