package com.epam.training.ticketservice.lib.user.persistence;

import com.epam.training.ticketservice.lib.user.persistence.base.UserBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "appAdmin")
public class ApplicationAdmin extends UserBase {
    public ApplicationAdmin(Long uid, String uname, String pw){
        super(uid, uname, pw);
    }
}
