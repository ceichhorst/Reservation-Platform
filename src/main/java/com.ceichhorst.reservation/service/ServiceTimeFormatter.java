package com.ceichhorst.reservation.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceTimeFormatter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    public List<ServiceInstance> formatTimes(List<ServiceInstance> services) {
        return services.stream().map(s -> {
            if (s.getServiceTime() != null) {
                s.setServiceTimeFormatted(
                        s.getServiceTime().format(FORMATTER)
                );
            }

            if (s.getEndTime() != null) {
                s.setEndTimeFormatted(
                        s.getEndTime().format(FORMATTER)
                );
            }

            return s;
        }).collect(Collectors.toList());
    }
}
