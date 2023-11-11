package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    Optional<Screening> findByMovieTitleAndRoomNameAndStartTime(String title, String room, LocalDateTime start);

    boolean existsByMovieTitleAndRoomNameAndStartTime(String title, String room, LocalDateTime start);

    List<Screening> findAllByRoomName(String roomName);
}
