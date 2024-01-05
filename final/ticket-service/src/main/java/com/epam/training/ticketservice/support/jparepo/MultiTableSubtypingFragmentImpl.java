package com.epam.training.ticketservice.support.jparepo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.core.EntityInformation;

@RequiredArgsConstructor
public abstract class MultiTableSubtypingFragmentImpl<S, T extends S, ID> implements MultiTableSubtypingFragment<S, T, ID> {
    protected final EntityInformation<T, ID> ei; //TODO this should fail for the same reason as in screeningalternatekeyfragmentimpl?

    public String getTableName(){ // This isnt correct but it should work for now
        return ei.getJavaType().getSimpleName();
    }
}
