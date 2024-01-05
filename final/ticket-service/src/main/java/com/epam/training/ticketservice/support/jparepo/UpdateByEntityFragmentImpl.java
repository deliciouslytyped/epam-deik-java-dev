package com.epam.training.ticketservice.support.jparepo;

import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.lang.NonNull;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


@RequiredArgsConstructor
public class UpdateByEntityFragmentImpl<T> implements UpdateByEntityFragment<T> {
    protected final EntityManager entityManager;
    protected final JpaMetamodelMappingContext mc;

    public void update(T e) {
        var pc = entityManager.createNamedStoredProcedureQuery(e.getClass().getSimpleName() + ".update");
        var pe = (BasicPersistentEntity) mc.getPersistentEntity(e.getClass());

        var ep = ((MetamodelImpl) (entityManager.getMetamodel())).entityPersister(e.getClass());
        String tname = null;
        if (ep instanceof SingleTableEntityPersister sep) {
            tname = sep.getTableName();
        } else {
            throw new UnsupportedOperationException("only set up for SingleTableEntityPersister");
        }
        //TODO see customjparepository, named parameters didnt work with hibernate storedprocedure because reasons
        //TODO so right now we just hope everything is in the right order
        //pc.setParameter("tablename", tname);
        pc.setParameter(1, tname);

        /*pe.doWithProperties((PropertyHandler) persistentProperty -> {
            try {
                var getter = persistentProperty.getGetter();
                var propname = persistentProperty.getName();
                pc.setParameter(propname + "name", propname);
                pc.setParameter(propname, getter.invoke(e));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });*/
        int[] i = {0}; // hack
        pe.doWithProperties((PropertyHandler) persistentProperty -> {
            try {
                var getter = persistentProperty.getGetter();
                var propname = getDbName(persistentProperty.getName());//TODO apparently parameters set in the "template" dont keep
                pc.setParameter(2*i[0]+1+1, propname); // one offset for 1-based, another for the first param
                pc.setParameter(2*i[0]+1+1+1, getter.invoke(e));
                i[0]++;
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });
        pc.executeUpdate();
        refreshEntity(e);
    }

    private String getDbName(String name) {
        return (new CamelCaseToUnderscoresNamingStrategy())
                .toPhysicalColumnName(new Identifier(name, false), null)
                .getCanonicalName(); //Probably the wrong method but close enough
    }

    protected <T> void refreshEntity(@NonNull T e){ //TODO check if its sufficent to use find()
        var pe = (BasicPersistentEntity)(mc.getPersistentEntity(e.getClass()));

        Serializable id = null;
        try {
            id = (Serializable) pe.getIdProperty().getGetter().invoke(e, new Object[]{});
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        var entity = entityManager.find(e.getClass(), id);
        entityManager.refresh(entity);
    }
}
