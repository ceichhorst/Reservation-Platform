package com.ceichhorst.reservation.service;

import java.time.LocalDate;

// Provides reservation total per service date
public class ServiceReservationStats {
    private Long reservationCount;
    private LocalDate serviceDate;
    private Long totalSeatsBooked;

    public ServiceReservationStats(LocalDate serviceDate, Long reservationCount, Long totalSeatsBooked) {
        this.serviceDate = serviceDate;
        this.reservationCount = reservationCount;
        this.totalSeatsBooked = totalSeatsBooked;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public Long getReservationCount() {
        return reservationCount;
    }

    public Long getTotalSeatsBooked() {
        return totalSeatsBooked;
    }
}
