package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.entity.ServiceInstance;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for formatting time fields in {@link ServiceInstance} objects.
 *
 * <p>This class converts {@link java.time.LocalTime} values into human-readable
 * string representations suitable for display in user interfaces.</p>
 *
 * <p>The formatted values are stored in the transient fields
 * {@code serviceTimeFormatted} and {@code endTimeFormatted} of each
 * {@link ServiceInstance}.</p>
 *
 * <p><strong>Note:</strong> This method mutates the provided {@link ServiceInstance}
 * objects by setting formatted fields. It does not create copies.</p>
 *
 * <p>The default format used is {@code h:mm a} (e.g., "6:30 PM").</p>
 *
 * @author ceichhorst
 */
public class ServiceTimeFormatter {

    /**
     * Formatter used to convert {@link java.time.LocalTime} values
     * into human-readable strings.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    /**
     * Applies formatted time strings to a list of service instances.
     *
     * <p>For each {@link ServiceInstance} in the list:</p>
     * <ul>
     *   <li>If {@code serviceTime} is not {@code null}, sets {@code serviceTimeFormatted}</li>
     *   <li>If {@code endTime} is not {@code null}, sets {@code endTimeFormatted}</li>
     * </ul>
     *
     * <p>The original list elements are modified in place and returned for convenience.</p>
     *
     * @param services the list of service instances to format
     * @return the same list of service instances with formatted time fields populated
     */
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
