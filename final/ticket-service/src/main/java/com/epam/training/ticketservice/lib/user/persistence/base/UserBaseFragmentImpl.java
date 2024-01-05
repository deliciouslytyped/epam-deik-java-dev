package com.epam.training.ticketservice.lib.user.persistence.base;

import com.epam.training.ticketservice.support.jparepo.MultiTableSubtypingFragmentImpl;
import org.springframework.data.repository.core.EntityInformation;

import javax.persistence.EntityManager;
import java.util.Optional;

public abstract class UserBaseFragmentImpl<T extends UserBase> extends MultiTableSubtypingFragmentImpl<UserBase,T,Long> implements UserBaseFragment<T> {

    protected final EntityManager em;

    public UserBaseFragmentImpl(EntityInformation<T, Long> ei, EntityManager em) {
        super(ei);
        this.em = em;
    }

    @Override
    public Optional<T> findByUsername(String username) {
        String jpql = "select a from " + getTableName() + " a where a.username = :username";
        return em.createQuery(jpql, ei.getJavaType())
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst();
    }
}
