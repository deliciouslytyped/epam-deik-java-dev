package com.epam.training.ticketservice.lib.user.persistence;

import com.epam.training.ticketservice.lib.booking.persistence.Booking;
import com.epam.training.ticketservice.lib.user.persistence.base.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Admin extends UserBase {
    public Admin(Long uid, String uname, String pw){
        super(uid, uname, pw);
    }
}
