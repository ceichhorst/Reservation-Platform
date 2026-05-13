package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.entity.ServiceInstance;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing aggregated reservation statistics
 * for a specific service date. A means to show an 'available/capacity' view.
 * Ex: '34/40 seats available'
 *
 * <p>This class is typically used as a projection result for reporting queries,
 * such as grouping reservations by {@link ServiceInstance} date.</p>
 *
 * <p>Each instance contains:</p>
 * <ul>
 *   <li>The service date</li>
 *   <li>The total number of reservations for that date</li>
 *   <li>The total number of seats booked (sum of all reservation party sizes)</li>
 * </ul>
 *
 * @author ceichhorst
 */
public class ServiceReservationStats {

    /**
     * The total number of reservations for the service date.
     */
    private Long reservationCount;

    /**
     * The date associated with the aggregated reservations.
     */
    private LocalDate serviceDate;

    /**
     * The total number of seats booked across all reservations for the date.
     */
    private Long totalSeatsBooked;

    /**
     * Constructs a new ServiceReservationStats instance.
     * @param serviceDate the date of the service
     * @param reservationCount the total number of reservations
     * @param totalSeatsBooked the total number of booked seats
     */
    public ServiceReservationStats(LocalDate serviceDate, Long reservationCount, Long totalSeatsBooked) {
        this.serviceDate = serviceDate;
        this.reservationCount = reservationCount;
        this.totalSeatsBooked = totalSeatsBooked;
    }

    /**
     * Gets the service date associated with these statistics.
     * @return the service date
     */
    public LocalDate getServiceDate() {
        return serviceDate;
    }

    /**
     * Gets the total number of reservations for the date.
     * @return the reservation count
     */
    public Long getReservationCount() {
        return reservationCount;
    }

    /**
     * Gets the total number of seats booked for the date.
     * @return the total seats booked
     */
    public Long getTotalSeatsBooked() {
        return totalSeatsBooked;
    }
}
