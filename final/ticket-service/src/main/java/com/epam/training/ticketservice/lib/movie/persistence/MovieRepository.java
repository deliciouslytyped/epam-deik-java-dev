package com.epam.training.ticketservice.lib.movie.persistence;

import com.epam.training.ticketservice.lib.db.UpdateByEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface MovieRepository extends JpaRepository<Movie, String> {
    @Transactional
    @Modifying
    @Query("update Movie m set m.genre = ?1, m.runtime = ?2 where m.title = ?3")
    void updateGenreAndRuntimeByTitle(@NonNull String genre, int runtime, @NonNull  String title);
}