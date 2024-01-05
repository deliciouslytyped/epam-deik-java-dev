package com.epam.training.ticketservice.lib.user.persistence;

import com.epam.training.ticketservice.lib.ticket.persistence.Ticket;
import com.epam.training.ticketservice.lib.user.persistence.base.UserBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

// TODO figure out what the correct setting is here
//  https://stackoverflow.com/questions/38572566/warning-equals-hashcode-on-lomboks-data-annotation-with-inheritance
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "appUser")
public class ApplicationUser extends UserBase {
    @OneToMany
    public Set<Ticket> bookings;

    public ApplicationUser(Long uid, String uname, String pw, Set<Ticket> bookings){
        super(uid, uname, pw);
        this.bookings = bookings;
    }
}
