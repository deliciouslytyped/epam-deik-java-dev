package com.epam.training.ticketservice.lib.pricing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurchargeRepository extends JpaRepository<Surcharge, String> {
}