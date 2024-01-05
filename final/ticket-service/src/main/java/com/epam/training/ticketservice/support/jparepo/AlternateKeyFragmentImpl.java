package com.epam.training.ticketservice.support.jparepo;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.lang.NonNull;

import javax.persistence.EntityManager;
import java.util.Optional;

public abstract class AlternateKeyFragmentImpl<T,ID,EID> implements AlternateKeyFragment<T,ID,EID>{
    @Autowired
    protected EntityManager em;

    @Override
    public Optional<T> getByAlternateKey(@NonNull Class<T> entityT, @NonNull ID altKey) {
        // TODO Hack to work around https://stackoverflow.com/questions/46377483/getting-entity-class-in-jpa-and-repository-interface
        var entityInformation = JpaEntityInformationSupport.getEntityInformation(entityT, em);

        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(entityInformation.getJavaType());
        var root = query.from(entityInformation.getJavaType());
        query
            .select(root)
            .where(cb.equal(getAlternateKey().apply(root), altKey));
        return em.createQuery(query).getResultList().stream().findAny();
    }

    @SneakyThrows
    @Override
    public void deleteByAlternateKey(ID altKey) {
        throw new Exception("asd");
    }
}
