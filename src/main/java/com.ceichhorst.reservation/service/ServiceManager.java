package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.dao.RestaurantDao;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.entity.SchedulingType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class ServiceManager {
    private ServiceInstanceDao serviceDao = new ServiceInstanceDao();
    private RestaurantDao restaurantDao = new RestaurantDao();
    private AdministratorDao adminDao = new AdministratorDao();

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

    public void updateSchedulingType(Long adminId, Long restaurantId, String schedulingType) {

        authorizeAdminAccess(adminId, restaurantId);

        Restaurant restaurant = restaurantDao.getById(restaurantId);

        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found.");
        }

        restaurant.setSchedulingType(SchedulingType.valueOf(schedulingType.toUpperCase()));
        restaurantDao.update(restaurant);
    }

    public void toggleVisibility(Long adminId, Long serviceId) {

        ServiceInstance service = serviceDao.getById(serviceId);

        if (service == null) {
            throw new IllegalArgumentException("Service not found.");
        }

        authorizeAdminAccess(adminId, service.getRestaurant().getId());

        service.setVisible(!service.getVisible());
        serviceDao.update(service);
    }

    public void authorizeAdminAccess(Long adminId, Long restaurantId) {
        Set<Long> restaurantIds = adminDao.getRestaurantIds(adminId);

        if (!restaurantIds.contains(restaurantId)) {
            throw new IllegalArgumentException("Unauthorized access to restaurant.");
        }
    }
}
