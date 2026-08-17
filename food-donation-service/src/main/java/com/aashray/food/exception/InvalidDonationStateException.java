package com.aashray.food.exception;

public class InvalidDonationStateException extends RuntimeException {
    public InvalidDonationStateException(String message) {
        super(message);
    }
}
