package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AvailabilityServiceTest {

    private AvailabilityService availabilityService;

    @BeforeAll
    void setup() {
        availabilityService = new AvailabilityService();
    }

    @Test
    void testBuildCalendar_singleDayAvailable() {

        List<ServiceInstance> services = new ArrayList<>();

        Restaurant restaurant = new Restaurant();
        ServiceInstance service = new ServiceInstance();
        service.setServiceDate(LocalDate.of(2026, 5, 1));
        service.setServiceTime(LocalTime.of(18, 0));
        service.setCapacity(10);
        service.setReservations(new ArrayList<>());

        services.add(service);

        List<DayAvailability> calendar = availabilityService.buildCalendar(services, restaurant.getSchedulingType());

        assertEquals(1, calendar.size());

        DayAvailability day = calendar.get(0);

        assertEquals(LocalDate.of(2026, 5, 1), day.getDate());
        assertEquals(10, day.getTotalSlots());
        assertEquals(0, day.getBookedSlots());
        assertTrue(day.isAvailable());
        assertFalse(day.isFull());

    }

    @Test
    void testBuildCalendar_withReservations() {

        List<ServiceInstance> services = new ArrayList<>();

        Restaurant restaurant = new Restaurant();
        ServiceInstance service = new ServiceInstance();
        service.setServiceDate(LocalDate.of(2026, 5, 1));
        service.setServiceTime(LocalTime.of(18, 0));
        service.setCapacity(10);

        List<Reservation> reservations = new ArrayList<>();

        Reservation r1 = new Reservation();
        r1.setPartySize(2);
        r1.setStatus(ReservationStatus.CONFIRMED);

        Reservation r2 = new Reservation();
        r2.setPartySize(3);
        r2.setStatus(ReservationStatus.CONFIRMED);

        reservations.add(r1);
        reservations.add(r2);

        service.setReservations(reservations);

        services.add(service);

        List<DayAvailability> calendar = availabilityService.buildCalendar(services, restaurant.getSchedulingType());

        assertEquals(1, calendar.size());

        DayAvailability day = calendar.get(0);

        assertEquals(10, day.getTotalSlots());
        assertEquals(5, day.getBookedSlots());
        assertTrue(day.isAvailable());
        assertFalse(day.isFull());

    }

    @Test
    void testBuildCalendar_fullDay() {

        List<ServiceInstance> services = new ArrayList<>();

        Restaurant restaurant = new Restaurant();
        ServiceInstance service = new ServiceInstance();
        service.setServiceDate(LocalDate.of(2026, 5, 1));
        service.setServiceTime(LocalTime.of(18, 0));
        service.setCapacity(5);

        List<Reservation> reservations = new ArrayList<>();

        Reservation r1 = new Reservation();
        r1.setPartySize(3);
        r1.setStatus(ReservationStatus.CONFIRMED);

        Reservation r2 = new Reservation();
        r2.setPartySize(2);
        r2.setStatus(ReservationStatus.CONFIRMED);

        reservations.add(r1);
        reservations.add(r2);

        service.setReservations(reservations);

        services.add(service);

        List<DayAvailability> calendar = availabilityService.buildCalendar(services, restaurant.getSchedulingType());

        assertEquals(1, calendar.size());

        DayAvailability day = calendar.get(0);

        assertEquals(5, day.getTotalSlots());
        assertEquals(5, day.getBookedSlots());
        assertTrue(day.isFull());
        assertFalse(day.isAvailable());

    }

    @Test
    void testBuildCalendar_multipleDatesGrouping() {

        List<ServiceInstance> services = new ArrayList<>();
        Restaurant restaurant = new Restaurant();

        // Day 1
        ServiceInstance s1 = new ServiceInstance();
        s1.setServiceDate(LocalDate.of(2026, 5, 4));
        s1.setCapacity(5);
        s1.setReservations(new ArrayList<>());

        // Day 2
        ServiceInstance s2 = new ServiceInstance();
        s2.setServiceDate(LocalDate.of(2026, 5, 1));
        s2.setCapacity(5);
        s2.setReservations(new ArrayList<>());

        services.add(s1);
        services.add(s2);

        List<DayAvailability> calendar = availabilityService.buildCalendar(services, restaurant.getSchedulingType());

        assertEquals(2, calendar.size());

        assertEquals(LocalDate.of(2026, 5, 1), calendar.get(0).getDate());
        assertEquals(LocalDate.of(2026, 5, 4), calendar.get(1).getDate());

    }

    @Test
    void testGetAvailableTimes_dateOnly_returnsSingleResults() {

        Restaurant restaurant = new Restaurant();
        restaurant.setSchedulingType(SchedulingType.DATE_ONLY);

        List<ServiceInstance> services = new ArrayList<>();

        ServiceInstance s1 = new ServiceInstance();
        s1.setServiceDate(LocalDate.of(2026, 5, 1));
        s1.setServiceTime(LocalTime.of(17, 0));
        s1.setCapacity(10);
        s1.setReservations(new ArrayList<>());

        ServiceInstance s2 = new ServiceInstance();
        s2.setServiceDate(LocalDate.of(2026, 5, 1));
        s2.setServiceTime(LocalTime.of(18, 0));
        s2.setCapacity(10);
        s2.setReservations(new ArrayList<>());

        services.add(s1);
        services.add(s2);

        List<ServiceInstance> result =
                availabilityService.getAvailableTimes(
                        restaurant,
                        services,
                        LocalDate.of(2026, 5, 1),
                        2
                );

        assertEquals(1, result.size());
        assertEquals(LocalTime.of(17, 0), result.get(0).getServiceTime());
    }

    @Test
    void testGetAvailableTimes_fixedTimeSlots_returnsAllAvailable() {

        Restaurant restaurant = new Restaurant();
        restaurant.setSchedulingType(SchedulingType.FIXED_TIME_SLOTS);

        List<ServiceInstance> services = new ArrayList<>();

        ServiceInstance s1 = new ServiceInstance();
        s1.setServiceDate(LocalDate.of(2026, 5, 1));
        s1.setServiceTime(LocalTime.of(17, 0));
        s1.setCapacity(10);
        s1.setReservations(new ArrayList<>());

        ServiceInstance s2 = new ServiceInstance();
        s2.setServiceDate(LocalDate.of(2026, 5, 1));
        s2.setServiceTime(LocalTime.of(18, 0));
        s2.setCapacity(10);
        s2.setReservations(new ArrayList<>());

        services.add(s1);
        services.add(s2);

        List<ServiceInstance> result =
                availabilityService.getAvailableTimes(
                        restaurant,
                        services,
                        LocalDate.of(2026, 5, 1),
                        2
                );

        assertEquals(2, result.size());
    }

    @Test
    void testGetAvailableTimes_dateTime_returnsSingleResults() {

        Restaurant restaurant = new Restaurant();
        restaurant.setSchedulingType(SchedulingType.DATE_TIME);

        List<ServiceInstance> services = new ArrayList<>();

        ServiceInstance s1 = new ServiceInstance();
        s1.setServiceDate(LocalDate.of(2026, 5, 1));
        s1.setServiceTime(LocalTime.of(17, 0));
        s1.setCapacity(10);
        s1.setReservations(new ArrayList<>());

        ServiceInstance s2 = new ServiceInstance();
        s2.setServiceDate(LocalDate.of(2026, 5, 1));
        s2.setServiceTime(LocalTime.of(18, 0));
        s2.setCapacity(10);
        s2.setReservations(new ArrayList<>());

        services.add(s1);
        services.add(s2);

        List<ServiceInstance> result =
                availabilityService.getAvailableTimes(
                        restaurant,
                        services,
                        LocalDate.of(2026, 5, 1),
                        2
                );

        assertEquals(2, result.size());

    }

    @Test
    void testGetAvailableTimes_filtersOutFullServices() {

        Restaurant restaurant = new Restaurant();
        restaurant.setSchedulingType(SchedulingType.FIXED_TIME_SLOTS);

        List<ServiceInstance> services = new ArrayList<>();

        ServiceInstance available = new ServiceInstance();
        available.setServiceDate(LocalDate.of(2026, 5, 1));
        available.setServiceTime(LocalTime.of(17, 0));
        available.setCapacity(10);
        available.setReservations(new ArrayList<>());

        ServiceInstance full = new ServiceInstance();
        full.setServiceDate(LocalDate.of(2026, 5, 1));
        full.setServiceTime(LocalTime.of(18, 0));
        full.setCapacity(4);

        Reservation reservation = new Reservation();
        reservation.setPartySize(4);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        full.setReservations(reservations);

        services.add(available);
        services.add(full);

        List<ServiceInstance> result =
                availabilityService.getAvailableTimes(
                        restaurant,
                        services,
                        LocalDate.of(2026, 5, 1),
                        2
                );

        assertEquals(1, result.size());
        assertEquals(LocalTime.of(17, 0), result.get(0).getServiceTime());
    }

    @Test
    void testGetAvailableTimes_filtersByPartySize() {

        Restaurant restaurant = new Restaurant();
        restaurant.setSchedulingType(SchedulingType.FIXED_TIME_SLOTS);

        List<ServiceInstance> services = new ArrayList<>();

        ServiceInstance service = new ServiceInstance();
        service.setServiceDate(LocalDate.of(2026, 5, 1));
        service.setServiceTime(LocalTime.of(18, 0));
        service.setCapacity(4);

        Reservation reservation = new Reservation();
        reservation.setPartySize(2);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        service.setReservations(reservations);

        services.add(service);

        List<ServiceInstance> result =
                availabilityService.getAvailableTimes(
                        restaurant,
                        services,
                        LocalDate.of(2026, 5, 1),
                        3
                );

        assertTrue(result.isEmpty());
    }
}
