package com.epam.training.ticketservice.lib.lib;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
// Lacking a more thorough implementation, we override individual methods we use to expose db constraint violations to reduce unnecessary db calls for initial lookup, etc (should we just be using raw-er db access at that point?)
public class CustomJpaRepository<T,ID> extends SimpleJpaRepository<T,ID> {
    private final EntityManager em;
    private final JpaEntityInformation<T,ID> entityInformation;
    public CustomJpaRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        em = entityManager;
        this.entityInformation = entityInformation;
    }

    // Not doing merge will will probably break stuff that expects save() to handle updates but it might just work for us.
    // (So don't do this in a larger application.)
    //  ... except we dont do it for delete because hibernate seems to do a bunch of object management work, making it ineffective to override, and doesnt propagate the db exception afaict?
    @Override
    public <S extends T> S save(S entity) {
        this.em.persist(entity);
        this.em.flush();
        return entity;
    }
}