package com.epam.training.ticketservice.lib.db;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorColumn;

// This could end up with a lot of columns if this wasn't just a toy application.
@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Attribute")
public class SingletonEntity {
    @Id
    public String attrName;
}
