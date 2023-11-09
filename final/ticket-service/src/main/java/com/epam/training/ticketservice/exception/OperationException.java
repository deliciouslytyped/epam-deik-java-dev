package com.epam.training.ticketservice.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OperationException extends Exception {

    public OperationException(String message) {
        super(message);
    }

    public OperationException(Exception cause) {
        super(cause);
    }

    public OperationException(String message, Exception cause) {
        super(message, cause);
    }
}
