package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.RestaurantDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.AvailabilityService;
import com.ceichhorst.reservation.service.AvailabilitySlot;
import com.ceichhorst.reservation.entity.ServiceInstance;
import com.ceichhorst.reservation.service.ServiceTimeFormatter;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Path("/availability")
@Produces(MediaType.APPLICATION_JSON)
public class AvailabilityController {

    private final RestaurantDao restaurantDao = new RestaurantDao();
    private final ServiceInstanceDao serviceDao = new ServiceInstanceDao();
    private final AvailabilityService availabilityService = new AvailabilityService();
    private final ServiceTimeFormatter formatter = new ServiceTimeFormatter();

    @GET
    public Response getAvailability(
            @QueryParam("restaurantId") Long restaurantId,
            @QueryParam("date") String dateStr,
            @QueryParam("partySize") Integer partySize
    ) {

        if (restaurantId == null || dateStr == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Missing parameters\"}")
                    .build();
        }

        int defaultPartySize = (partySize != null && partySize > 0) ? partySize : 1;

        try {
            LocalDate date = LocalDate.parse(dateStr);

            // Load domain data
            Restaurant restaurant = restaurantDao.getById(restaurantId);
            List<ServiceInstance> services = serviceDao.getByRestaurantId(restaurantId);

            // Filter out hidden services
            services = services.stream()
                    .filter(ServiceInstance::getVisible)
                    .collect(Collectors.toList());

            // Apply logic
            List<ServiceInstance> available =
                    availabilityService.getAvailableTimes(
                            restaurant,
                            services,
                            date,
                            defaultPartySize
                    );

            available = formatter.formatTimes(available);

            List<AvailabilitySlot> response = available.stream()
                    .map(s -> {
                        int booked = s.getReservations() == null ? 0 :
                                s.getReservations().stream()
                                .filter(r -> r.getStatus() != null &&
                                             (r.getStatus().name().equals("PENDING") ||
                                             r.getStatus().name().equals("CONFIRMED")))
                                .mapToInt(r -> r.getPartySize())
                                .sum();
                        int remaining = s.getCapacity() - booked;
                        return new AvailabilitySlot(s.getId(), s.getServiceTimeFormatted(), remaining);
                        })
                    .collect(Collectors.toList());

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to load availability\"}")
                    .build();
        }
    }
}