package com.epam.training.ticketservice.lib.room.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    private Long id;
}
