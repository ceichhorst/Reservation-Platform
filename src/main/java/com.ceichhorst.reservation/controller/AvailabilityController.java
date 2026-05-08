package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.RestaurantDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.AvailabilityService;
import com.ceichhorst.reservation.service.AvailabilitySlot;
import com.ceichhorst.reservation.service.ServiceInstance;
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
            @QueryParam("date") String dateStr
    ) {

        if (restaurantId == null || dateStr == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Missing parameters\"}")
                    .build();
        }

        try {
            LocalDate date = LocalDate.parse(dateStr);

            // Load domain data
            Restaurant restaurant = restaurantDao.getById(restaurantId);
            List<ServiceInstance> services = serviceDao.getByRestaurantId(restaurantId);

            // Apply logic
            List<ServiceInstance> available =
                    availabilityService.getAvailableTimes(
                            restaurant,
                            services,
                            date
                    );

            available = formatter.formatTimes(available);

            List<AvailabilitySlot> response = available.stream()
                    .map(s -> new AvailabilitySlot(
                            s.getId(),
                            s.getServiceTimeFormatted()
                    ))
                    .collect(Collectors.toList());

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to load availability\"}")
                    .build();
        }
    }
}