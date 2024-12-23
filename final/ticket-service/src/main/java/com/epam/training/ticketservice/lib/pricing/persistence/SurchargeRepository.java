package com.epam.training.ticketservice.lib.pricing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SurchargeRepository extends JpaRepository<Surcharge, String> {

    @Query("select s from Surcharge s where s.name = ?1")
    Optional<Surcharge> findByName(@NonNull String name);
}