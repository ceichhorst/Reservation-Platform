package com.ceichhorst.reservation.service;

import java.time.LocalTime;
import java.time.LocalDate;

/**
 * DTO representing aggregated reservation stats for a service date's time slot
 *
 * @author ceichhorst
 */
public class TimeSlotReservationStats {

    private LocalDate serviceDate;
    private LocalTime serviceTime;
    private Long reservationCount;
    private Long totalSeatsBooked;

    public TimeSlotReservationStats(LocalDate serviceDate, LocalTime serviceTime,
                                    Long reservationCount, Long totalSeatsBooked) {
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
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
