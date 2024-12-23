package com.epam.training.ticketservice.lib.pricing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MovieSurchargeRepository extends JpaRepository<MovieSurchargeMap, Long> {
    @Transactional
    @Query("select m from MovieSurchargeMap m where m.movie.title = ?1")
    List<MovieSurchargeMapInfo> findByMovie_Title(@NonNull String title);
}