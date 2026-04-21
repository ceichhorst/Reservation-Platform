package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.entity.Reservation;

public class ReservationResult {

    private boolean success;
    private String message;
    private Reservation reservation;

    private ReservationResult(boolean success, String message, Reservation reservation) {
        this.success = success;
        this.message = message;
        this.reservation = reservation;
    }

    private static ReservationResult success(Reservation reservation) {
        return new ReservationResult(true, null, reservation);
    }

    private ReservationResult failure(Reservation reservation) {
        return new ReservationResult(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
