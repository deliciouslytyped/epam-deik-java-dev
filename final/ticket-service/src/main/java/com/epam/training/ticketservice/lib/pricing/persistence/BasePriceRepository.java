package com.epam.training.ticketservice.lib.pricing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePriceRepository extends JpaRepository<BasePrice, String> {
}