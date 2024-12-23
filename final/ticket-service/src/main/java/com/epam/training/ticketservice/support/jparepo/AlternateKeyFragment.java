package com.epam.training.ticketservice.support.jparepo;

import org.springframework.data.repository.core.EntityInformation;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.Optional;
import java.util.function.Function;

//TODO shouldnt ID and EID be the same?
@Transactional
public interface AlternateKeyFragment<T,ID,EID> { //EID is used in the implementation to get access to EntityInformation
    Function<Root<T>, Path<ID>> getAlternateKey();
    Optional<T> getByAlternateKey(@NonNull Class<T> entityT, ID altKey);
    void deleteByAlternateKey(ID altKey);
}
