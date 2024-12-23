package com.epam.training.ticketservice.lib.pricing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface BasePriceRepository extends JpaRepository<BasePrice, String> {
    @Transactional
    @Modifying
    @Query("update BasePrice b set b.basePrice = ?1")
    void updateBasePriceBy(@NonNull Integer basePrice);
}