package com.epam.training.ticketservice.lib.room.persistence;

import com.epam.training.ticketservice.support.UpdateByEntityFragment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomCrudRepository extends JpaRepository<Room, String>, UpdateByEntityFragment<Room> {
}
