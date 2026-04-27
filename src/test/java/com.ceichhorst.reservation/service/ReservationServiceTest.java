package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.testutils.TestDatabase;
import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationServiceTest {

    private SessionFactory sessionFactory;
    private ReservationService reservationService;
    private ReservationDao reservationDao;

    @BeforeAll
    void setup() {
        reservationDao = new ReservationDao();
        reservationService = new ReservationService();

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

    @Test
    void testCreateReservation_success() {

        ServiceInstance instance =
                new ServiceInstanceDao().getServicesByRestaurantOnDate(1L, LocalDate.now())
                        .get(0);

        ReservationResult result = reservationService.createReservation(
                1L,
                instance.getServiceDate().toString(),
                instance.getServiceTime().toString(),
                2,
                "John Doe",
                "john@email.com",
                "N/A",
                "None"
        );

        assertNotNull(result);
        assertTrue(result.isSuccess());

        Reservation created = result.getReservation();

        assertEquals("John Doe", created.getCustomerName());
        assertEquals(2, created.getPartySize());
        assertEquals("CONFIRMED", created.getStatus().name());

    }

}
