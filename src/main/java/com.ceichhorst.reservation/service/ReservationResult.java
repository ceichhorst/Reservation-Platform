package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.entity.Reservation;

/**
 * Represents the result of a reservation creation attempt.
 *
 * <p>This class is used to encapsulate both successful and failed reservation
 * operations in a consistent return type, avoiding the use of exceptions for
 * expected business outcomes such as capacity constraints or invalid input.</p>
 *
 * <p>A result can represent either:</p>
 * <ul>
 *   <li><strong>Success:</strong> Contains the created {@link Reservation}</li>
 *   <li><strong>Failure:</strong> Contains an error message explaining why the operation failed</li>
 * </ul>
 *
 * @author ceichhorst
 */
public class ReservationResult {

    /**
     * Indicates whether the reservation operation was successful.
     */
    private boolean success;

    /**
     * Human-readable message describing the result.
     *
     * <p>Typically populated only in failure cases.</p>
     */
    private String message;

    /**
     * The created reservation, present only when the operation is successful.
     */
    private Reservation reservation;

    /**
     * Private constructor to enforce use of factory methods.
     * @param success whether the operation succeeded
     * @param message optional message describing the result
     * @param reservation the created reservation, or {@code null} if failed
     */
    private ReservationResult(boolean success, String message, Reservation reservation) {
        this.success = success;
        this.message = message;
        this.reservation = reservation;
    }

    /**
     * Creates a successful reservation result.
     * @param reservation the successfully created reservation
     * @return a success result containing the reservation
     */
    public static ReservationResult success(Reservation reservation) {
        return new ReservationResult(true, null, reservation);
    }

    /**
     * Creates a failed reservation result.
     * @param message a description of why the reservation failed
     * @return a failure result containing an error message
     */
    public static ReservationResult failure(String message) {
        return new ReservationResult(false, message, null);
    }

    /**
     * Indicates whether the reservation operation succeeded.
     * @return {@code true} if successful; {@code false} otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the result message.
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the created reservation.
     * @return the reservation if successful
     */
    public Reservation getReservation() {
        return reservation;
    }
}
