package com.epam.training.ticketservice.core.screening.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening,Integer> {

    Optional<Screening> findByMovieNameAndRoomNameAndDate(String movieName, String roomName, String date);

    Long deleteByMovieNameAndRoomNameAndDate(String movieName, String roomName, LocalDateTime date);
}
