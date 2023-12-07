package com.epam.training.ticketservice.lib.db;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

public class CustomJpaRepository<T, ID> extends SimpleJpaRepository<T,ID> {
    private final EntityManager em;
    
    public CustomJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        em = entityManager;
    }

    // Not doing merge will will probably break stuff that expects save() to handle updates but it might just work for us.
    // (So don't do this in a larger application.)
    @Override
    public <S extends T> S save(S entity) {
        this.em.persist(entity);
        return entity;
    }
}