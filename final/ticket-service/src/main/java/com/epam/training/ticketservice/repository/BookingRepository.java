package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByScreeningMovieAndScreeningRoomAndScreeningStartTime(Movie movie, Room room,
                                                                               LocalDateTime startTime);

    List<Booking> findAllByScreening(Screening screening);
}
