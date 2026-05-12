package com.ceichhorst.reservation.entity;

/**
 * Enumeration representing the possible action states of a reservation by an admin.
 *
 * <p>This status is used to track the action an admin provides to a reservation from initial
 * creation through confirmation or cancellation.</p>
 *
 * <ul>
 *     <li>{@link #CREATED} - The reservation has been created but not yet confirmed by an admin.</li>
 *     <li>{@link #CONFIRMED} - The reservation has been fully submitted and confirmed by and admin.</li>
 *     <li>{@link #CANCELLED} - The reservation has been cancelled by and admin.</li>
 *     <li>{@link #UPDATED} - The reservation has been updated by an admin.</li>
 * </ul>
 *
 * @author ceichhorst
 */
public enum ReservationActionType {
    CREATED,
    CONFIRMED,
    CANCELLED,
    UPDATED
}
