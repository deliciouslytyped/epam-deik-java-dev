package com.epam.training.ticketservice.lib.user;

import com.epam.training.ticketservice.lib.reservation.model.ReservationDto;
import com.epam.training.ticketservice.lib.reservation.persistence.ReservationKey;
import com.epam.training.ticketservice.lib.user.model.AdminDto;
import com.epam.training.ticketservice.lib.user.model.UserCreationDto;
import com.epam.training.ticketservice.lib.user.model.UserDto;
import com.epam.training.ticketservice.support.exceptions.AlreadyExistsException;
import com.epam.training.ticketservice.support.exceptions.ApplicationDomainException;

public class Exceptions {
    public static void throwAlreadyExists(AdminDto arg){
        throw new AlreadyExistsException(arg.toString()); //TODO
    }

    public static void throwAlreadyExists(UserCreationDto arg){
        throw new AlreadyExistsException(arg.toString()); //TODO
    }

    public static void throwNotExist(AdminDto arg){
        throw new ApplicationDomainException(arg.toString());
    }

    public static void throwAlreadyExists(UserDto arg){
        throw new AlreadyExistsException(arg.toString()); //TODO
    }

    public static void throwNotExist(UserDto arg){
        throw new ApplicationDomainException(arg.toString());
    }

    public static void throwNotExistWithContext(Long id, Exception e) {
        throw new ApplicationDomainException("The account with id" + id + " does not exist.", e);
    }
}
