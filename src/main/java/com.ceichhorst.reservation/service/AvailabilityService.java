package com.ceichhorst.reservation.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;

// Query logic (read)
public class AvailabilityService {

    public List<DayAvailability> buildCalendar(List<ServiceInstance> services) {
        Map<LocalDate, List<ServiceInstance>> grouped = new HashMap<>();

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
