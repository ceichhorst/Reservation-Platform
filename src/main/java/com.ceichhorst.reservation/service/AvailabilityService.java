package com.ceichhorst.reservation.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;

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

            DayAvailability day = new DayAvailability();
            day.setDate(date);

            day.setAvailable(!dayServices.isEmpty());
            calendar.add(day);


        }
        return calendar;
    }
}
