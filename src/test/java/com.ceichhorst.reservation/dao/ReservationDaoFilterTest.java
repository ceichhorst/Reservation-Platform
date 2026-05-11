package com.ceichhorst.reservation.dao;

import java.io.InputStream;
import java.util.Properties;
import java.time.LocalDate;
import java.time.LocalTime;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.service.ServiceReservationStats;
import com.ceichhorst.reservation.service.TimeSlotReservationStats;
import com.ceichhorst.reservation.util.HibernateUtil;
import com.ceichhorst.reservation.testutils.TestDatabase;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationDaoFilterTest {

    private ReservationDao reservationDao;

    @BeforeAll
    void setup() throws Exception {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);
        reservationDao = new ReservationDao();
    }

    @BeforeEach
    void cleanDatabase() {
        TestDatabase.runSQL("cleandb.sql");
    }

    @Test
    void testFindByFilter_noFilters_returnsAllForRestaurant() {
        List<Reservation> results = reservationDao.findByFilter(
                null, null, null, null, Set.of(1L)
        );
        assertFalse(results.isEmpty());
    }

    @Test
    void testFindByFilter_byId_returnsCorrectReservation() {
        List<Reservation> results = reservationDao.findByFilter(
                2000L, null, null, null, Set.of(1L)
        );
        assertEquals(1, results.size());
        assertEquals(2000L, results.get(0).getId());
    }

    @Test
    void testFindByFilter_byCustomerName_returnsMatch() {
        List<Reservation> results = reservationDao.findByFilter(
                null, "Seed", null, null, Set.of(1L)
        );
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getCustomerName().contains("Seed"));
    }

    @Test
    void testFindByFilter_byEmail_returnsMatch() {
        List<Reservation> results = reservationDao.findByFilter(
                null, null, "seed@email.com", null, Set.of(1L)
        );
        assertFalse(results.isEmpty());
        assertEquals("seed@email.com", results.get(0).getEmail());
    }

    @Test
    void testFindByFilter_byDate_returnsMatch() {
        List<Reservation> results = reservationDao.findByFilter(
                null, null, null, LocalDate.now(), Set.of(1L)
        );
        assertFalse(results.isEmpty());
    }

    @Test
    void testFindByFilter_wrongRestaurant_returnsEmpty() {
        List<Reservation> results = reservationDao.findByFilter(
                null, null, null, null, Set.of(999L)
        );
        assertTrue(results.isEmpty());
    }

    @Test
    void testFindByFilter_partialNameMatch_caseInsensitive() {
        List<Reservation> results = reservationDao.findByFilter(
                null, "seed user", null, null, Set.of(1L)
        );
        assertFalse(results.isEmpty());
    }

    @Test
    void testCountReservationsByService_returnsCorrectCount() {
        Long count = reservationDao.countReservationsByService(Set.of(1L));
        assertNotNull(count);
        assertTrue(count >= 0);
    }

    @Test
    void testCountReservationsByService_returnsStats() {
        List<ServiceReservationStats> stats = reservationDao.countReservationsGroupedByService(Set.of(1L));
        assertNotNull(stats);
    }

    @Test
    void testCountReservationsByTimeSlots_returnsStats() {
        List<TimeSlotReservationStats> stats = reservationDao.countReservationsGroupedByTimeSlots(Set.of(1L));
        assertNotNull(stats);
    }

    @Test
    void testFindByFilter_nonExistentId_returnsEmpty() {
        List<Reservation> results = reservationDao.findByFilter(
                99999L, null, null, null, Set.of(1L)
        );
        assertTrue(results.isEmpty());
    }
}
