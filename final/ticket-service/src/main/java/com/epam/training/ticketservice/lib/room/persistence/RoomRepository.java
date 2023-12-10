package com.epam.training.ticketservice.lib.room.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    @Transactional
    @Modifying
    @Query("update Room r set r.rowCount = ?1, r.colCount = ?2 where r.name = ?3")
    void updateRowCountAndColCountByName(@NonNull Integer rowCount, @NonNull Integer colCount, @NonNull String name);
}
