package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.testutils.TestDatabase;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.entity.Restaurant;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AvailabilityServiceTest {

    private SessionFactory sessionFactory;
    private AvailabilityService availabilityService;

    @BeforeAll
    void setup() {
        availabilityService = new AvailabilityService();
    }

    @BeforeAll
    void setupSessionFactory() throws Exception {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);

    }

    @BeforeEach
    void cleanDatabase() {
        TestDatabase.runSQL("cleandb.sql");
    }

    @AfterAll
    void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
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
}
