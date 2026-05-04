package com.ceichhorst.reservation.entity;

/**
 * Enumeration representing the possible lifecycle states of a reservation.
 *
 * <p>This status is used to track the progression of a reservation from initial
 * creation through confirmation or cancellation.</p>
 *
 * <ul>
 *     <li>{@link #PENDING} - The reservation has been created but not yet confirmed.</li>
 *     <li>{@link #CONFIRMED} - The reservation has been submitted and confirmed.</li>
 *     <li>{@link #CANCELLED} - The reservation has been cancelled and is no longer active.</li>
 * </ul>
 *
 * @author ceichhorst
 */
public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
}