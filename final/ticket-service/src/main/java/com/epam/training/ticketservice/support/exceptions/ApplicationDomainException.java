package com.epam.training.ticketservice.lib.util.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApplicationDomainException extends RuntimeException {

    public ApplicationDomainException(String message) {
        super(message);
    }

    public ApplicationDomainException(Exception cause) {
        super(cause);
    }

    public ApplicationDomainException(String message, Exception cause) {
        super(message, cause);
    }
}
