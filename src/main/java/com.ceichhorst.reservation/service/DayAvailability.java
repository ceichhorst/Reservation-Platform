package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.entity.ServiceInstance;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Data Transfer Object (DTO) representing aggregated availability for a single day.
 *
 * <p>This class is used to convey a summarized view of reservation availability
 * for a specific date, typically as part of a calendar or scheduling UI.</p>
 *
 * <p>Each instance contains both capacity and booking information derived from
 * one or more {@link ServiceInstance} objects:</p>
 * <ul>
 *   <li>Total available slots (capacity)</li>
 *   <li>Total booked slots (sum of reservation party sizes)</li>
 *   <li>Flags indicating whether the day is fully booked or still available</li>
 * </ul>
 *
 * @author ceichhorst
 */
public class DayAvailability {

    /**
     * The date represented by this availability summary.
     */
    private LocalDate date;

    /**
     * Indicates whether any capacity remains for this date.
     */
    private boolean available;

    /**
     * Indicates whether the date is fully booked.
     */
    private boolean full;

    /**
     * Total capacity across all service instances for the date.
     */
    private int totalSlots;

    /**
     * Total number of booked slots (sum of all reservation party sizes).
     */
    private int bookedSlots;

    /**
     * Pre-slot availability breakdown based on scheduling type / time
     */
    private List<CalendarTimeSlot> slots = new ArrayList<>();

    /**
     * Gets the date associated with this availability.
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date associated with this availability.
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Indicates whether the date has any remaining availability.
     * @return {@code true} if at least one slot is available; {@code false} otherwise
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets whether the date has remaining availability.
     * @param available {@code true} if capacity remains; {@code false} otherwise
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Indicates whether the date is fully booked.
     * @return {@code true} if fully booked; {@code false} otherwise
     */
    public boolean isFull() {
        return full;
    }

    /**
     * Sets whether the date is fully booked.
     * @param full {@code true} if fully booked; {@code false} otherwise
     */
    public void setFull(boolean full) {
        this.full = full;
    }

    /**
     * Gets the total capacity for the date.
     * @return the total number of available slots
     */
    public int getTotalSlots() {
        return totalSlots;
    }

    /**
     * Sets the total capacity for the date.
     * @param totalSlots the total number of available slots
     */
    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    /**
     * Gets the total number of booked slots for the date.
     * @return the number of booked slots
     */
    public int getBookedSlots() {
        return bookedSlots;
    }

    /**
     * Sets the total number of booked slots for the date.
     * @param bookedSlots the number of booked slots
     */
    public void setBookedSlots(int bookedSlots) {
        this.bookedSlots = bookedSlots;
    }

    public List<CalendarTimeSlot> getSlots() {
        return slots;
    }

    public void setSlots(List<CalendarTimeSlot> slots) {
        this.slots = slots;
    }
}
