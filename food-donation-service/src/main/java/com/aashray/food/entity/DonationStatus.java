package com.aashray.food.entity;

/**
 * PENDING       -> created by donor, visible to NGOs
 * ACCEPTED      -> an NGO accepted it
 * REJECTED      -> an NGO rejected it (goes back to PENDING pool or stays closed, see service logic)
 * PICKED_UP     -> NGO has physically collected it
 * DELIVERED     -> distributed to beneficiaries, terminal state
 * EXPIRED       -> passed expiryTime without being picked up, terminal state
 */
public enum DonationStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    PICKED_UP,
    DELIVERED,
    EXPIRED
}
