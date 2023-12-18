package com.epam.training.ticketservice.support.exceptions;

public class AlreadyExistsException extends ApplicationDomainException {

    public AlreadyExistsException(String what) {
        super(what + " already exists.");
    }
}
