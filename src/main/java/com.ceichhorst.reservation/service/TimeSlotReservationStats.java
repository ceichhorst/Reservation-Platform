package com.ceichhorst.reservation.service;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * DTO representing aggregated reservation stats for a service date's time slot
 *
 * @author ceichhorst
 */
public class TimeSlotReservationStats {

    /**
     * Formatter for time
     */
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    private LocalDate serviceDate;
    private LocalTime serviceTime;
    private String serviceTimeFormatted;
    private Long reservationCount;
    private Long totalSeatsBooked;

    public TimeSlotReservationStats(LocalDate serviceDate, LocalTime serviceTime,
                                    Long reservationCount, Long totalSeatsBooked) {
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.serviceTimeFormatted = serviceTime != null
                ? serviceTime.format(FORMATTER)
                : "";
        this.reservationCount = reservationCount;
        this.totalSeatsBooked = totalSeatsBooked;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public LocalTime getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(LocalTime serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceTimeFormatted() {
        return serviceTimeFormatted;
    }

    public void setServiceTimeFormatted(String serviceTimeFormatted) {
        this.serviceTimeFormatted = serviceTimeFormatted;
    }

    public Long getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(Long reservationCount) {
        this.reservationCount = reservationCount;
    }

    public Long getTotalSeatsBooked() {
        return totalSeatsBooked;
    }

    public void setTotalSeatsBooked(Long totalSeatsBooked) {
        this.totalSeatsBooked = totalSeatsBooked;
    }
}
