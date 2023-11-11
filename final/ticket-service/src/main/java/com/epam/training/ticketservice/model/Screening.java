package com.epam.training.ticketservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Entity
@Table(name = "screenings")
@NoArgsConstructor
public class Screening {
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    private Room room;
    @Column(nullable = false)
    private LocalDateTime startTime;

    @OneToMany(mappedBy = "screening", fetch = FetchType.EAGER)
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "component_id", referencedColumnName = "id")
    private PriceComponent priceComponent;

    public Screening(Movie movie, Room room, LocalDateTime startTime) {
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
    }

    public boolean isOverlapping(Screening other, int breakTime) {
        var start = getStartTime();
        var end = start.plusMinutes(movie.getLength() + breakTime);
        var otherStart = other.getStartTime();
        var otherEnd = otherStart.plusMinutes(other.getMovie().getLength() + breakTime);

        return start.isBefore(otherStart) && end.isAfter(otherStart)
                || start.isAfter(otherStart) && end.isBefore(otherEnd);
    }
}
