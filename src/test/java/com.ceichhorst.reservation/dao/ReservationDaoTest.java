package com.ceichhorst.reservation.dao;

import java.io.InputStream;
import java.util.Properties;
import java.time.LocalDate;
import java.time.LocalTime;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceTemplate;
import com.ceichhorst.reservation.entity.SchedulingType;
import com.ceichhorst.reservation.util.HibernateUtil;
import com.ceichhorst.reservation.testutils.TestDatabase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationDaoTest {

    private SessionFactory sessionFactory;
    private ReservationDao reservationDao;

    @BeforeAll
    void setupSessionFactory() throws Exception {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);

        reservationDao = new ReservationDao();
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
    void testSaveReservation() {
        Session session = sessionFactory.openSession();
        ServiceInstance service = session.get(ServiceInstance.class, 1000L);
        session.close();

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
        Reservation fetched = reservationDao.getById(2000L);

        assertNotNull(fetched);
        assertEquals("Seed User", fetched.getCustomerName());
    }

    @Test
    void testDeleteReservation() {
        Reservation reservation = reservationDao.getById(2000L);
        assertNotNull(reservation);

        reservationDao.delete(reservation);

        Reservation deleted = reservationDao.getById(2000L);
        assertNull(deleted);
    }

    @Test
    void testUpdateReservation() {
        Reservation reservation = reservationDao.getById(2000L);

        reservation.setCustomerName("Updated Name");
        reservation.setPartySize(4);

        reservationDao.update(reservation);

        Reservation updated = reservationDao.getById(2000L);
        assertEquals("Updated Name", updated.getCustomerName());
        assertEquals(4, updated.getPartySize());

    }

    @Test
    void testCreateReservationIfAvailable() {
        Session session = sessionFactory.openSession();
        ServiceInstance service = session.get(ServiceInstance.class, 1000L);
        session.close();

        Reservation reservation = new Reservation();
        reservation.setCustomerName("Available Test");
        reservation.setEmail("test@email.com");
        reservation.setPartySize(4);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setServiceInstance(service);

        boolean created = reservationDao.createReservationIfAvailable(reservation);

        assertTrue(created);
    }

    @Test
    void testCreateReservationIfAvailable_failure() {
        Session session = sessionFactory.openSession();
        ServiceInstance service = session.get(ServiceInstance.class, 1000L);
        session.close();

        Reservation reservation1 = new Reservation();
        reservation1.setCustomerName("Full Test");
        reservation1.setEmail("full@email.com");
        reservation1.setPartySize(8);
        reservation1.setStatus(ReservationStatus.CONFIRMED);
        reservation1.setServiceInstance(service);

        reservationDao.createReservationIfAvailable(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setCustomerName("Should Fail");
        reservation2.setEmail("fail@email.com");
        reservation2.setPartySize(1);
        reservation2.setServiceInstance(service);

        boolean created = reservationDao.createReservationIfAvailable(reservation2);

        assertFalse(created);
    }
}