package com.epam.training.ticketservice.lib.movie.persistence;

import com.epam.training.ticketservice.support.jparepo.UpdateByEntityFragment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

//TODO I think this comes from the spring configuration jpa base class override...//extends CustomJpaRepository<Movie,String> {

public interface MovieCrudRepository extends JpaRepository<Movie, String>, UpdateByEntityFragment<Movie> {
    @Query("select m from Movie m where m.title = ?1")
    Optional<Movie> findByTitle(String title);
}
