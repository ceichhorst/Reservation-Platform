package com.ceichhorst.reservation.entity;

/**
 * Enumeration defining the scheduling model used by a restaurant for its services.
 *
 * <p>This type determines how customers select reservation times and how
 * {@code ServiceInstance} objects are structured and presented.</p>
 *
 * <ul>
 *     <li>{@link #DATE_ONLY} - Reservations are made for a specific date only, having times set/determined
 *     by admins prior to a reservation going live.</li>
 *     <li>{@link #DATE_TIME} - Reservations are made for a specific date and time, having times on a date set within a
 *     range by admins, broken into 15 minutes intervals.</li>
 *     <li>{@link #FIXED_TIME_SLOTS} - Reservations are limited to predefined time slots,
 *     configured in advance by admins. More freedom on when times are compared to DATE_TIME</li>
 * </ul>
 *
 * <p>This value influences both user interface behavior and backend validation logic.</p>
 *
 * @author ceichhorst
 */
public enum SchedulingType {
    DATE_ONLY,
    DATE_TIME,
    FIXED_TIME_SLOTS
}