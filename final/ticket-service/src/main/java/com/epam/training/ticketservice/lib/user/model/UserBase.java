package com.epam.training.ticketservice.lib.user.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

//TODO make sure the uid sequence is shared
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class UserBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long uid;

    @Column(nullable = false)
    public String username;

    @Column(nullable = false)
    public String password;
}
