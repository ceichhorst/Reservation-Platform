package com.ceichhorst.reservation.service;

import java.time.LocalTime;

/**
 * Represents the availability summary of a single service instance time slot
 *
 * @author ceichhorst
 */
public class CalendarTimeSlot {

    /**
     * The start time of the slot
     */
    private LocalTime serviceTime;

    /**
     * Formatted display version of the start time
     */
    private String serviceTimeFormatted;

    /**
     * Total capacity of the slot
     */
    private int remainingSeats;

    /**
     * Whether the slot is full
     */
    private boolean full;

    public String getServiceTimeFormatted() {
        return serviceTimeFormatted;
    }

    public void setServiceTimeFormatted(String serviceTimeFormatted) {
        this.serviceTimeFormatted = serviceTimeFormatted;
    }

    public LocalTime getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(LocalTime serviceTime) {
        this.serviceTime = serviceTime;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public int getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    /**
     * Helper method to get the hour of the service time
     */
    public int getHour() {
        return serviceTime != null ? serviceTime.getHour() : -1;
    }

}
