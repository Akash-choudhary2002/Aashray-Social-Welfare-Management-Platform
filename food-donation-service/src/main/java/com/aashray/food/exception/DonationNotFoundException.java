package com.aashray.food.exception;

public class DonationNotFoundException extends RuntimeException {
    public DonationNotFoundException(Long id) {
        super("Food donation not found with id: " + id);
    }
}
