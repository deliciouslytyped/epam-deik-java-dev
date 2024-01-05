package com.epam.training.ticketservice.lib.user.persistence.base;

import com.epam.training.ticketservice.support.jparepo.MultiTableSubtypingFragment;

import java.util.Optional;

public interface UserBaseFragment<T extends UserBase> extends MultiTableSubtypingFragment<UserBase,T,Long> {
    Optional<T> findByUsername(String username);
}
