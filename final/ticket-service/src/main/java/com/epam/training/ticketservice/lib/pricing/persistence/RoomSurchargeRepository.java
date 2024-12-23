package com.epam.training.ticketservice.lib.pricing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RoomSurchargeRepository extends JpaRepository<RoomSurchargeMap, Long> {
    @Transactional //TODO are these actually needed or do they get added implcitly?
    @Query("select r from RoomSurchargeMap r where r.room.name = ?1")
    List<RoomSurchargeMapInfo> findByRoom_Name(@NonNull String name);
}