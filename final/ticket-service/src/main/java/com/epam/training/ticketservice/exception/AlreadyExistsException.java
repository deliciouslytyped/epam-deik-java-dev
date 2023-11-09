package com.epam.training.ticketservice.exception;

public class AlreadyExistsException extends OperationException {

    public AlreadyExistsException(String what) {
        super(what + " already exists");
    }
}
