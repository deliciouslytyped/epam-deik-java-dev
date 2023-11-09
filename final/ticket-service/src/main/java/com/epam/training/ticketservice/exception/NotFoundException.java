package com.epam.training.ticketservice.exception;

public class NotFoundException extends OperationException {

    public NotFoundException(String what) {
        super(what + " not found");
    }
}
