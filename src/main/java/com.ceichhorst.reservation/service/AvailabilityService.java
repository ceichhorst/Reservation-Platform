package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.entity.SchedulingType;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

/**
 * Service responsible for computing availability of {@link ServiceInstance} objects.
 *
 * <p>This class contains read-only business logic used to determine which service
 * instances are available for booking, as well as aggregating availability into
 * calendar-style summaries.</p>
 *
 * <p>Availability is determined based on:</p>
 * <ul>
 *   <li>The requested service date</li>
 *   <li>The restaurant's {@link SchedulingType}</li>
 *   <li>The remaining capacity of each {@link ServiceInstance}</li>
 * </ul>
 *
 * @author ceichhorst
 */
public class AvailabilityService {

    /**
     * Retrieves available service instances for a given restaurant and date.
     *
     * <p>This method filters the provided list of services by:</p>
     * <ul>
     *   <li>Matching the specified {@code serviceDate}</li>
     *   <li>Ensuring the service has remaining capacity</li>
     * </ul>
     *
     * <p>The result is then adjusted based on the restaurant's
     * {@link SchedulingType}:</p>
     * <ul>
     *   <li>{@code DATE_ONLY}: Returns at most one available service (date-level booking)</li>
     *   <li>{@code FIXED_TIME_SLOTS}: Returns all available time slots</li>
     *   <li>{@code DATE_TIME}: Returns all available time slots</li>
     * </ul>
     *
     * <p>Results are ordered by service start time.</p>
     *
     * @param restaurant the restaurant whose scheduling rules apply
     * @param services the list of service instances to evaluate
     * @param serviceDate the date for which availability is requested
     * @return a list of available service instances
     */
    public List<ServiceInstance> getAvailableTimes(
            Restaurant restaurant,
            List<ServiceInstance> services,
            LocalDate serviceDate,
            int partySize
    ) {
        SchedulingType type = restaurant.getSchedulingType();

        List<ServiceInstance> filtered = services.stream()
                .filter(s -> s.getServiceDate().equals(serviceDate))
                .filter(s -> {
                    int booked = s.getReservations() == null ? 0 :
                            s.getReservations().stream()
                            .filter(Reservation::isActive)
                            .mapToInt(Reservation::getPartySize)
                            .sum();

                    int remaining = s.getCapacity() - booked;

                    return remaining >= partySize;
                })
                .sorted(Comparator.comparing(ServiceInstance::getServiceTime))
                .collect(Collectors.toList());

        switch (type) {
            case DATE_ONLY:
                return filtered.stream().limit(1).collect(Collectors.toList());

            case FIXED_TIME_SLOTS:
                return filtered;

            case DATE_TIME:
                return filtered;

            default:
                return new ArrayList<>();
        }
    }

    /**
     * Builds a calendar-style view of availability grouped by date.
     *
     * <p>This method aggregates a list of {@link ServiceInstance} objects into
     * daily summaries represented by {@link DayAvailability} objects.</p>
     *
     * <p>For each date, the following values are calculated:</p>
     * <ul>
     *   <li>Total capacity (sum of all service capacities)</li>
     *   <li>Total booked slots (sum of all reservation party sizes)</li>
     *   <li>Whether the day is fully booked</li>
     *   <li>Whether the day has any remaining availability</li>
     * </ul>
     *
     * <p>Results are ordered chronologically by date.</p>
     *
     * @param services the list of service instances to aggregate
     * @return a list of {@link DayAvailability} objects representing daily availability
     */
    public List<DayAvailability> buildCalendar(List<ServiceInstance> services) {
        Map<LocalDate, List<ServiceInstance>> grouped = new TreeMap<>();

        for (ServiceInstance s : services) {
            LocalDate date = s.getServiceDate();

            grouped.computeIfAbsent(date, d -> new ArrayList<>()).add(s);
        }

        List<DayAvailability> calendar = new ArrayList<>();

        for (Map.Entry<LocalDate, List<ServiceInstance>> entry : grouped.entrySet()) {

            LocalDate date = entry.getKey();
            List<ServiceInstance> dayServices = entry.getValue();

            int total = 0;
            int booked = 0;

            for (ServiceInstance s : dayServices) {
                total += s.getCapacity();

                if (s.getReservations() != null) {
                    booked += s.getReservations()
                            .stream()
                            .filter(Reservation::isActive)
                            .mapToInt(Reservation::getPartySize)
                            .sum();
                }
            }

            DayAvailability day = new DayAvailability();
            day.setDate(date);
            day.setTotalSlots(total);
            day.setBookedSlots(booked);
            day.setFull(booked >= total);
            day.setAvailable(booked < total);

            calendar.add(day);

        }
        return calendar;
    }
}
