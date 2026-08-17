package com.aashray.food.exception;

public class UnauthorizedDonationAccessException extends RuntimeException {
    public UnauthorizedDonationAccessException(String message) {
        super(message);
    }
}
