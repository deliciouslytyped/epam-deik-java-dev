package com.epam.training.ticketservice.lib.screening.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
}