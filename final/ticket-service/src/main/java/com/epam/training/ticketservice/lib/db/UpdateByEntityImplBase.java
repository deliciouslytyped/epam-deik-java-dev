package com.epam.training.ticketservice.lib.db;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.lang.NonNull;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


@RequiredArgsConstructor
public class UpdateByEntityImplBase {
    protected final EntityManager entityManager;
    protected final JpaMetamodelMappingContext mc;
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
