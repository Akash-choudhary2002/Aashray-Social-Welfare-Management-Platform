package com.aashray.education.entity;

/**
 * ACTIVE     -> educator currently teaching this student this subject
 * COMPLETED  -> educator marked the assignment finished
 * CANCELLED  -> NGO cancelled the assignment before completion
 */
public enum AssignmentStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}
