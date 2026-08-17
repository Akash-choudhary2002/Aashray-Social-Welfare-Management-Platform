package com.aashray.monetary.exception;

public class UnauthorizedDonationAccessException extends RuntimeException {
    public UnauthorizedDonationAccessException(String message) {
        super(message);
    }
}
