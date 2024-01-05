package com.epam.training.ticketservice.support.jparepo;

import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.Query;

public interface MultiTableSubtypingFragment<S, T extends S, ID> {
}
