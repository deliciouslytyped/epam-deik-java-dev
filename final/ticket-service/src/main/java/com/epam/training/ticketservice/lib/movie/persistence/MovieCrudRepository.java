package com.epam.training.ticketservice.lib.movie.persistence;

import com.epam.training.ticketservice.support.jparepo.UpdateByEntityFragment;
import org.springframework.data.jpa.repository.JpaRepository;

//TODO I think this comes from the spring configuration jpa base class override...//extends CustomJpaRepository<Movie,String> {

public interface MovieCrudRepository extends JpaRepository<Movie, String>, UpdateByEntityFragment<Movie> {
}
