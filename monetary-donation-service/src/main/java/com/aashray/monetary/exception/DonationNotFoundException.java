package com.aashray.monetary.exception;

public class DonationNotFoundException extends RuntimeException {
    public DonationNotFoundException(Long id) {
        super("Monetary donation not found with id: " + id);
    }

    public DonationNotFoundException(String message) {
        super(message);
    }
}
