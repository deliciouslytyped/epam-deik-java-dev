package com.epam.training.ticketservice.lib.db;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
    @Override
    public <S extends T> S save(S entity) {
        this.em.persist(entity);
        return entity;
    }
}