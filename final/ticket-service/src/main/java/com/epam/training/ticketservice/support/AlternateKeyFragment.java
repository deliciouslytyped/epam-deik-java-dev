package com.epam.training.ticketservice.support;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.Optional;
import java.util.function.Function;

public interface AlternateKeyFragment<T,ID,EID> { //EID is used in the implementation to get access to EntityInformation
    Function<Root<T>, Path<ID>> getAlternateKey();
    Optional<T> getByAlternateKey(ID altKey);
    void deleteByAlternateKey(ID altKey);
}
