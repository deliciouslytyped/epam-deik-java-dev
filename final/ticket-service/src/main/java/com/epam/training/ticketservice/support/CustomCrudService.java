package com.epam.training.ticketservice.support;

import com.epam.training.ticketservice.lib.security.aspects.DefaultPrivileged;

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

/**
 * See also ServiceByAlternateKey
 * @param <T>
 * @param <ID>
 */
@DefaultPrivileged
public interface CustomCrudService<T,ID,M extends CustomMapper> {
    M getMapper(); //TODO should I really have this in here? Needed it for the presentation layer to create dtos, but it pollutes the type signature.
    void create(T entityDto);
    Optional<T> get(ID id);
    //TODO does this interface force PITA behavior? are there any better alternatives?
    void update(T entityDto);
    void delete(ID id);

    List<T> list();
}
