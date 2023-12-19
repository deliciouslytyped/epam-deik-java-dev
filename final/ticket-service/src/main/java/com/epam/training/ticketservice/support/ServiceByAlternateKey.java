package com.epam.training.ticketservice.support;

import java.util.Optional;

public interface ServiceByAlternateKey<T,ID> {
    Optional<T> getByAlternateKey(ID id);
    void deleteByAlternateKey(ID id);

}
