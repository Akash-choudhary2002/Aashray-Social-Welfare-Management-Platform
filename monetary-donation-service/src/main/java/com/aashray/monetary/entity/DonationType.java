package com.aashray.monetary.entity;

/**
 * ONE_TIME  -> a single Razorpay-charged donation (the only flow this
 *              service currently drives end to end)
 * RECURRING -> reserved for a future subscription/UPI-autopay flow;
 *              accepted by the schema now so that feature doesn't need
 *              another migration later
 */
public enum DonationType {
    ONE_TIME,
    RECURRING
}
