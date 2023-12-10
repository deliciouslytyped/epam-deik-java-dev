package com.epam.training.ticketservice.lib.movie.persistence;

import com.epam.training.ticketservice.lib.db.UpdateByEntity;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class UpdateByEntityMovieImpl implements UpdateByEntityMovie {
    private final EntityManager entityManager;
    @Override
    public void update(Movie m) {
        entityManager.createNamedQuery("Movie.update")
                .setParameter("title", m.getTitle())
                .setParameter("genre", m.getGenre())
                .setParameter("runtime", m.getRuntime())
                .executeUpdate();
    }
}
