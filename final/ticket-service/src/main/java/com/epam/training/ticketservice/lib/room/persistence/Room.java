package com.epam.training.ticketservice.lib.room.persistence;

import com.epam.training.ticketservice.support.CheckConstraint;
import com.epam.training.ticketservice.support.GenUpdateByEntityFragment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Data
@GenUpdateByEntityFragment()
@AllArgsConstructor
@NoArgsConstructor
@Entity
@CheckConstraint(name="CHECK_ROW_COUNT", check="ROW_COUNT > 0")
@CheckConstraint(name="CHECK_COL_COUNT", check="COL_COUNT > 0") //TODO spel or jpql or what, substitution?
@Table(name = "room")
public class Room {
    @Id
    private String name;

    @Column(nullable = false)
    private Integer rowCount;

    @Column(nullable = false)
    private Integer colCount;
}

