package com.epam.training.ticketservice.lib.pricing.persistence;

import com.epam.training.ticketservice.lib.screening.persistence.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ScreeningSurchargeRepository extends JpaRepository<ScreeningSurchargeMap, Long> {
    @Transactional
    @Query("select s from ScreeningSurchargeMap s where s.screening = ?1")
    List<ScreeningSurchargeMapInfo> findByScreening(Screening screening);

}