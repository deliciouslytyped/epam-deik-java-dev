package com.epam.training.ticketservice.lib.util.exceptions;

public class AlreadyExistsException extends ApplicationDomainException {

    public AlreadyExistsException(String what) {
        super(what + " already exists.");
    }
}
