package com.aashray.volunteer.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Volunteer task not found with id: " + id);
    }
}
