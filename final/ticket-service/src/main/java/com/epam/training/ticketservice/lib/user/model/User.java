package com.epam.training.ticketservice.lib.user.model;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

// TODO figure out what the correct setting is here
//  https://stackoverflow.com/questions/38572566/warning-equals-hashcode-on-lomboks-data-annotation-with-inheritance
@EqualsAndHashCode(callSuper = true)
@Data

@Entity
@Table(name = "appUser")
public class User extends UserBase {
    @OneToMany
    public Set<Booking> bookings;
}
