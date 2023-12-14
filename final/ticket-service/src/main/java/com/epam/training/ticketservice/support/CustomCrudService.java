package com.epam.training.ticketservice.lib.lib;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// <DTO, DAO Entity, DAO ID>
public interface CustomCrudService<T,U,ID,R extends JpaRepository<U,ID> & UpdateByEntity<U>> {
    void create(T entityDto);
    Optional<T> get(ID id);
    //TODO does this interface force PITA behavior? are there any better alternatives?
    void update(T entityDto);
    void delete(ID id);
    List<T> list();
}
