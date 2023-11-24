package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.MovieService;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
public class Screening {

    @Id
    @GeneratedValue
    private Integer id;
    @Getter
    private String movieName;
    @Getter
    private String roomName;
    private LocalDateTime date;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Screening(String movieName, String roomName, String dateTimeString) {
        this.movieName = movieName;
        this.roomName = roomName;
        this.date = LocalDateTime.parse(dateTimeString,formatter);
    }

    public String getFormattedDate() {
        return date.format(formatter);
    }
}
