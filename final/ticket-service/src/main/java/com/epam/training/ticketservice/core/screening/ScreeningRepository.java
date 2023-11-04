package com.epam.training.ticketservice.core.screening;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening,Integer> {

    Optional<Screening> findByMovieNameAndRoomNameAndDate(String movieName, String roomName, Date date);

    Long deleteByMovieNameAndRoomNameAndDate(String movieName,String roomName,Date date);
}
