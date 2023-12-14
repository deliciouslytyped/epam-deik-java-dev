package com.epam.training.ticketservice.lib.util.exceptions;

public class NotFoundException extends ApplicationDomainException {

    public NotFoundException(String what) {
        super(what + " not found");
    }
}
