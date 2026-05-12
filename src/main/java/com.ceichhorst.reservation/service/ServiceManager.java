package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.dao.RestaurantDao;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.entity.SchedulingType;
import com.ceichhorst.reservation.entity.ServiceInstance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

/**
 * Service layer responsible for administrative management of {@link ServiceInstance} entities.
 *
 * <p>This class provides functionality for creating, modifying, and deleting
 * service instances, as well as updating scheduling configurations and visibility.</p>
 *
 * <p>All operations enforce authorization by verifying that the given administrator
 * has access to the target {@link Restaurant}.</p>
 *
 * <p><strong>Scheduling behavior:</strong> Service creation logic varies depending on the
 * restaurant's {@link SchedulingType}:</p>
 * <ul>
 *   <li>{@code DATE_ONLY}: Creates a single service for the entire date</li>
 *   <li>{@code FIXED_TIME_SLOTS}: Creates a single service at a specified time</li>
 *   <li>{@code DATE_TIME}: Generates multiple services in 15-minute increments within a time range</li>
 * </ul>
 *
 * @author ceichhorstg
 */
public class ServiceManager {
    private ServiceInstanceDao serviceDao = new ServiceInstanceDao();
    private RestaurantDao restaurantDao = new RestaurantDao();
    private AdministratorDao adminDao = new AdministratorDao();

    /**
     * Adds one or more service instances for a restaurant based on its scheduling type.
     *
     * <p>This method first verifies that the administrator has permission to manage
     * the specified restaurant. It then creates service instances according to the
     * restaurant's {@link SchedulingType}:</p>
     *
     * <ul>
     *   <li><strong>DATE_ONLY:</strong> Creates a single service at midnight representing the entire day</li>
     *   <li><strong>FIXED_TIME_SLOTS:</strong> Creates a single service at the specified time</li>
     *   <li><strong>DATE_TIME:</strong> Creates multiple services at 15-minute intervals between
     *       {@code time} (inclusive) and {@code endTime} (exclusive)</li>
     * </ul>
     *
     * <p>All created services are initialized as visible and assigned the specified capacity.</p>
     *
     * @param adminId the ID of the administrator performing the action
     * @param restaurantId the ID of the restaurant
     * @param date the date of the service(s)
     * @param time the start time (required for FIXED_TIME_SLOTS and DATE_TIME)
     * @param endTime the end time (required for DATE_TIME)
     * @param capacity the maximum number of guests for each service instance
     *
     * @throws IllegalArgumentException if:
     * <ul>
     *   <li>The administrator is not authorized for the restaurant</li>
     *   <li>The restaurant does not exist</li>
     *   <li>Required time parameters are missing or invalid</li>
     * </ul>
     */
    public void addService(Long adminId, Long restaurantId, LocalDate date, LocalTime time, LocalTime endTime, int capacity) {

        // Authorize admin access
        authorizeAdminAccess(adminId, restaurantId);

        Restaurant restaurant = restaurantDao.getById(restaurantId);

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found.");
        }

        SchedulingType type = restaurant.getSchedulingType();

        switch (type) {

            case DATE_ONLY : {
                ServiceInstance service = new ServiceInstance();
                service.setRestaurant(restaurant);
                service.setServiceDate(date);;
                service.setServiceTime(LocalTime.MIDNIGHT);
                service.setEndTime(null);
                service.setCapacity(capacity);
                service.setVisible(true);
                serviceDao.save(service);
                break;
            }

            case FIXED_TIME_SLOTS : {
                ServiceInstance service = new ServiceInstance();
                service.setRestaurant(restaurant);
                service.setServiceDate(date);;
                service.setServiceTime(time);
                service.setEndTime(null);
                service.setCapacity(capacity);
                service.setVisible(true);
                serviceDao.save(service);
                break;
            }

            case DATE_TIME : {
                if (time == null || endTime == null) {
                    throw new IllegalArgumentException("Start and end time required");
                }

                if (!time.isBefore(endTime)) {
                    throw new IllegalArgumentException("Start time must be before end time");
                }

                LocalTime cursor = time;

                while(cursor.isBefore(endTime)) {
                    ServiceInstance service = new ServiceInstance();
                    service.setRestaurant(restaurant);
                    service.setServiceDate(date);
                    service.setServiceTime(cursor);
                    service.setEndTime(endTime);
                    service.setCapacity(capacity);
                    service.setVisible(true);

                    serviceDao.save(service);

                    cursor = cursor.plusMinutes(15);
                }
            }
        }
    }

    /**
     * Deletes a service instance if it has no associated reservations.
     *
     * <p>This method verifies administrator authorization and ensures that
     * the service instance does not have any existing reservations before deletion.</p>
     *
     * @param adminId the ID of the administrator performing the action
     * @param serviceId the ID of the service instance to delete
     *
     * @throws IllegalArgumentException if the service does not exist or access is unauthorized
     * @throws IllegalStateException if the service has existing reservations
     */
    public void deleteService(Long adminId, Long serviceId) {

        ServiceInstance service = serviceDao.getById(serviceId);

        if (service == null) {
            throw new IllegalArgumentException("Service not found.");
        }

        authorizeAdminAccess(adminId, service.getRestaurant().getId());

        if (serviceDao.hasReservations(serviceId)) {
            throw new IllegalStateException("Cannot delete a service with reservations.");
        }

        serviceDao.delete(service);
    }

    /**
     * Updates the scheduling type of a restaurant.
     *
     * <p>This affects how future service instances should be created and interpreted.</p>
     *
     * @param adminId the ID of the administrator performing the action
     * @param restaurantId the ID of the restaurant
     * @param schedulingType the new scheduling type (case-insensitive)
     *
     * @throws IllegalArgumentException if the restaurant does not exist, the scheduling type is invalid,
     * or access is unauthorized
     */
    public void updateSchedulingType(Long adminId, Long restaurantId, String schedulingType) {

        authorizeAdminAccess(adminId, restaurantId);

        Restaurant restaurant = restaurantDao.getById(restaurantId);

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found.");
        }

        restaurant.setSchedulingType(SchedulingType.valueOf(schedulingType.toUpperCase()));
        restaurantDao.update(restaurant);
    }

    /**
     * Toggles the visibility of a service instance.
     *
     * <p>Invisible services are typically hidden from booking interfaces but
     * remain in the system.</p>
     *
     * @param adminId the ID of the administrator performing the action
     * @param serviceId the ID of the service instance
     *
     * @throws IllegalArgumentException if the service does not exist or access is unauthorized
     */
    public void toggleVisibility(Long adminId, Long serviceId) {

        ServiceInstance service = serviceDao.getById(serviceId);

        if (service == null) {
            throw new IllegalArgumentException("Service not found.");
        }

        authorizeAdminAccess(adminId, service.getRestaurant().getId());

        service.setVisible(!service.getVisible());
        serviceDao.update(service);
    }

    /**
     * Verifies that an administrator has access to a specific restaurant.
     *
     * <p>This method retrieves all restaurant IDs associated with the administrator
     * and checks whether the target restaurant is included.</p>
     *
     * @param adminId the administrator ID
     * @param restaurantId the restaurant ID to validate access for
     *
     * @throws IllegalArgumentException if the administrator does not have access
     */
    public void authorizeAdminAccess(Long adminId, Long restaurantId) {
        Set<Long> restaurantIds = adminDao.getRestaurantIds(adminId);

        if (!restaurantIds.contains(restaurantId)) {
            throw new IllegalArgumentException("Unauthorized access to restaurant.");
        }
    }
}
