package com.epam.training.ticketservice.support;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @param <T> DTO
 * @param <U> JPA DAO Entity
 * @param <ID> JPA DAO Entity key
 * @param <R> JPA Repository
 */

//The type parameters aren't used here but they are used in the Impl //TODO arguably that means they shouldnt be used here
public interface CustomCrudService<T,ID> {
    void create(T entityDto);
    Optional<T> get(ID id);
    //TODO does this interface force PITA behavior? are there any better alternatives?
    void update(T entityDto);
    void delete(ID id);
    List<T> list();
}
