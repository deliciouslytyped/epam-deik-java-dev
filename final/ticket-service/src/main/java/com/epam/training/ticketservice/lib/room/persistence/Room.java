package com.epam.training.ticketservice.lib.room.persistence;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "room")
public class Room {
    @Id
    private String roomName;

    @Column(nullable = false)
    private Integer rowCount;

    @Column(nullable = false)
    private Integer colCount;
}
