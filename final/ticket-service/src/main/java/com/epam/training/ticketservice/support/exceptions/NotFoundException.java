package com.epam.training.ticketservice.support.exceptions;

public class NotFoundException extends ApplicationDomainException {

    public NotFoundException(String what) {
        super(what + " not found");
    }
}
