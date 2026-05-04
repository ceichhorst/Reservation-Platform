package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.entity.SchedulingType;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

// Query logic (read)
public class AvailabilityService {

    public List<ServiceInstance> getAvailableTimes(
            Restaurant restaurant,
            List<ServiceInstance> services,
            LocalDate serviceDate
    ) {
        SchedulingType type = restaurant.getSchedulingType();

        List<ServiceInstance> filtered = services.stream()
                .filter(s -> s.getServiceDate().equals(serviceDate))
                .filter(s -> {
                    int booked = s.getReservations() == null ? 0 :
                            s.getReservations().stream()
                            .mapToInt(r -> r.getPartySize())
                            .sum();
                    return booked < s.getCapacity();
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
                            .mapToInt(r -> r.getPartySize())
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
