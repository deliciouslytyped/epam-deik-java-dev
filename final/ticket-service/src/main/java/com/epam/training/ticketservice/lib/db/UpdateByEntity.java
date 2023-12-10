package com.epam.training.ticketservice.lib.db;

import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

//TODO this is / is this semantically broken? - using entities here , they are managed (well, after saving) hibernate objects so...?
public interface UpdateByEntity<T> {
    @Transactional //Does this even work if not inheriting from JpaRepository
    void update(T entity); //TODO IDK how to force implementing this in a way the compiler will notice (as opposed to inheriting the interface) so you need to be aware of it
}
