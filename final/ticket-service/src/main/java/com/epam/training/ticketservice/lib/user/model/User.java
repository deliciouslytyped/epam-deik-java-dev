package com.epam.training.ticketservice.lib.user.model;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = "appUser")
public class User extends UserBase {
    @OneToMany
    public Set<Booking> bookings;
}
