package com.epam.training.ticketservice.lib.user.model;

import lombok.Data;

import javax.persistence.*;

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
