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
    private int capacity;

    /**
     * Number of seats booked in teh slot
     */
    private int booked;

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

    public int getBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
