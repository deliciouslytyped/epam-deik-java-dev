package com.epam.training.ticketservice.lib.movie.persistence;

import com.epam.training.ticketservice.lib.db.UpdateByEntityImplBase;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import javax.persistence.EntityManager;

public class UpdateByEntityMovieImpl extends UpdateByEntityImplBase implements UpdateByEntityMovie {

    public UpdateByEntityMovieImpl(EntityManager entityManager, JpaMetamodelMappingContext mc) {
        super(entityManager, mc);
    }

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
}
