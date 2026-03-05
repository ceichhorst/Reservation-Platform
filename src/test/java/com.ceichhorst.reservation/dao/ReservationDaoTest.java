package com.ceichhorst.reservation.dao;

import java.io.InputStream;
import java.util.Properties;
import java.time.LocalDate;
import java.time.LocalTime;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceTemplate;
import com.ceichhorst.reservation.util.HibernateUtil;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationDaoTest {

    private SessionFactory testSessionFactory;
    private ReservationDao reservationDao;
    private ServiceInstanceDao serviceDao;

    @BeforeAll
    void setupSessionFactory() throws Exception {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find database.properties");
            }
            properties.load(input);
        }
        Configuration configuration = new Configuration();

        // Load entity classes
        configuration.addAnnotatedClass(Reservation.class);
        configuration.addAnnotatedClass(ServiceInstance.class);
        configuration.addAnnotatedClass(Restaurant.class);
        configuration.addAnnotatedClass(ServiceTemplate.class);
        configuration.addProperties(properties);

        testSessionFactory = configuration.buildSessionFactory();

        HibernateUtil.setSessionFactory(testSessionFactory);

    }

    @BeforeEach
    void setupDaos() {
        reservationDao = new ReservationDao();
        serviceDao = new ServiceInstanceDao();
    }

    @AfterAll
    void tearDown() {
        if (testSessionFactory != null) {
            testSessionFactory.close();
        }
    }

    private ServiceInstance createTestService(int capacity) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");

        RestaurantDao restaurantDao = new RestaurantDao();
        restaurantDao.save(restaurant);

        ServiceInstance service = new ServiceInstance();
        service.setCapacity(capacity);
        service.setRestaurant(restaurant);
        service.setServiceDate(LocalDate.now());
        service.setServiceTime(LocalTime.now());
        serviceDao.save(service);
        return service;
    }

    @Test
    void testSaveReservation() {
        ServiceInstance service = createTestService(10);

        Reservation reservation = new Reservation();
        reservation.setCustomerName("Test User");
        reservation.setEmail("test@email.com");
        reservation.setPartySize(2);
        reservation.setServiceInstance(service);

        reservationDao.save(reservation);

        assertNotNull(reservation.getId());
    }

    @Test
    void testGetById() {
        ServiceInstance service = createTestService(10);

        Reservation reservation = new Reservation();
        reservation.setCustomerName("Get Test");
        reservation.setEmail("test@email.com");
        reservation.setPartySize(3);
        reservation.setServiceInstance(service);

        reservationDao.save(reservation);

        Reservation fetched = reservationDao.getById(reservation.getId());

        assertNotNull(fetched);
        assertEquals("Get Test", fetched.getCustomerName());
    }

    @Test
    void testDeleteReservation() {
        ServiceInstance service = createTestService(10);

        Reservation reservation = new Reservation();
        reservation.setCustomerName("Delete Test");
        reservation.setEmail("test@email.com");
        reservation.setPartySize(2);
        reservation.setServiceInstance(service);

        reservationDao.save(reservation);
        int id = reservation.getId();

        reservationDao.delete(reservation);

        Reservation deleted = reservationDao.getById(id);

        assertNull(deleted);
    }

    @Test
    void testUpdateReservation() {
        ServiceInstance service = createTestService(10);

        Reservation reservation = new Reservation();
        reservation.setCustomerName("Original Name");
        reservation.setEmail("original@email.com");
        reservation.setPartySize(2);
        reservation.setServiceInstance(service);

        reservationDao.save(reservation);
        int id = reservation.getId();

        reservation.setCustomerName("Updated Name");
        reservation.setPartySize(4);

        reservationDao.update(reservation);

        Reservation updated = reservationDao.getById(id);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.getCustomerName());
        assertEquals(4, updated.getPartySize());
    }

    @Test
    void testCreateReservationIfAvailable() {
        ServiceInstance service = createTestService(10);

        boolean created = reservationDao.createReservationIfAvailable(
                service.getId(), 4, "Available Test", "test@email.com");
        assertTrue(created);
    }

    @Test
    void testCreateReservationIfAvailable_failure() {
        ServiceInstance service = createTestService(5);

        reservationDao.createReservationIfAvailable(
                service.getId(), 5, "Full Test", "full@email.com");

        boolean created = reservationDao.createReservationIfAvailable(
            service.getId(), 1, "Should Fail", "fail@email.com");

        assertFalse(created);
    }
}