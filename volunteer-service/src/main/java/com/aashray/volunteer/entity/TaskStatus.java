package com.aashray.volunteer.entity;

/**
 * ASSIGNED     -> NGO assigned the task, volunteer hasn't started yet
 * IN_PROGRESS  -> volunteer has started working on it
 * COMPLETED    -> volunteer finished the task, terminal state
 * CANCELLED    -> NGO cancelled the task before completion, terminal state
 */
public enum TaskStatus {
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
