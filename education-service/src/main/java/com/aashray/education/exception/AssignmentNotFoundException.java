package com.aashray.education.exception;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(Long id) {
        super("Education assignment not found with id: " + id);
    }
}
