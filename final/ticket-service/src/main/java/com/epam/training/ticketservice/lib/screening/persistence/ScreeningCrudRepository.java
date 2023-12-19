package com.epam.training.ticketservice.lib.screening.persistence;

import com.epam.training.ticketservice.support.AlternateKeyFragment;
import com.epam.training.ticketservice.support.UpdateByEntityFragment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningCrudRepository extends JpaRepository<Screening, Long>, UpdateByEntityFragment<Screening>, ScreeningAlternateKeyFragment {
}
