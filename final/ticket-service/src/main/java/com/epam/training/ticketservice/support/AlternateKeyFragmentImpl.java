package com.epam.training.ticketservice.support;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.lang.NonNull;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.PluralAttribute;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AlternateKeyFragmentImpl<T,ID,EID> implements AlternateKeyFragment<T,ID,EID>{
    protected EntityManager em;
    protected EntityInformation<T,EID> entityInformation;

    @Override
    public Optional<T> getByAlternateKey(@NonNull ID altKey) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(entityInformation.getJavaType());
        var root = query.from(entityInformation.getJavaType());
        query
            .select(root)
            .where(cb.equal(getAlternateKey().apply(root), altKey));
        return em.createQuery(query).getResultList().stream().findAny();
    }

    @Override
    public void deleteByAlternateKey(ID altKey) {

    }
}
