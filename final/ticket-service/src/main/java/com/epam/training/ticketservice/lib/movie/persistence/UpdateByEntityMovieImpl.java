package com.epam.training.ticketservice.lib.movie.persistence;

import com.epam.training.ticketservice.lib.db.UpdateByEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.internal.SessionImpl;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;

@RequiredArgsConstructor
public class UpdateByEntityMovieImpl implements UpdateByEntityMovie {
    private final EntityManager entityManager;
    @Override
    public void update(Movie m) {
        entityManager.createNamedStoredProcedureQuery("Movie.update")
                .setParameter("tablename", "movie") // TODO HAcksss
                .setParameter("titlename", "title")
                .setParameter("title", m.getTitle())
                .setParameter("genrename", "genre")
                .setParameter("genre", m.getGenre())
                .setParameter("runtimename", "runtime")
                .setParameter("runtime", m.getRuntime())
                .executeUpdate();
        refreshEntity(m);
    }

    void refreshEntity(Movie m){
        var session = (SessionImpl)(entityManager.unwrap(org.hibernate.Session.class).getSession()); //TODO is there a generic way to do stuff with the session?
        var persister = session.getEntityPersister("Movie", m);
        var entity = session.getPersistenceContext().getEntity(session.generateEntityKey(m.getTitle(), persister));
        entityManager.refresh(entity);
    }
}
