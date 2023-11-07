package com.epam.training.ticketservice.core.book;

import com.epam.training.ticketservice.core.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    Optional<Book> findByMovieNameAndRoomNameAndDateAndSeatsIn(String movieName, String roomName, String date, List<Integer> seats);

}
