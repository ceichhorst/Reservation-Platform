package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ServiceInstance;
import com.ceichhorst.reservation.testutils.TestDatabase;
import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.time.LocalDate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationServiceTest {

    private SessionFactory sessionFactory;
    private ReservationService reservationService;

    @BeforeAll
    void setup() {

        EmailService mockEmailService = mock(EmailService.class);

        doNothing().when(mockEmailService).sendReservationConfirmation(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyString(),
                anyString()
        );

        reservationService = new ReservationService(mockEmailService);

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
                instance.getId(),
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

    @Test
    void testCreateReservation_invalidServiceInstance_returnsFailure() {

        ReservationResult result = reservationService.createReservation(
                1L,
                99999L,
                2,
                "John Doe",
                "john@email.com",
                "N/A",
                "None"
        );

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Invalid time selection", result.getMessage());
    }

    @Test
    void testCreateReservation_invalidRestaurant_returnsFailure() {

        ServiceInstance instance =
                new ServiceInstanceDao()
                        .getServicesByRestaurantOnDate(1L, LocalDate.now())
                        .get(0);

        ReservationResult result = reservationService.createReservation(
                99999L,
                instance.getId(),
                2,
                "John Doe",
                "john@email.com",
                "N/A",
                "None"
        );

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Invalid restaurant", result.getMessage());
    }

    @Test
    void testCreateReservation_fullCapacity_returnsFailure() {

        ServiceInstance instance =
                new ServiceInstanceDao()
                        .getServicesByRestaurantOnDate(1L, LocalDate.now())
                        .get(0);

        ReservationResult first = reservationService.createReservation(
                1L,
                instance.getId(),
                8,
                "First User",
                "first@email.com",
                "N/A",
                "None"
        );

        assertTrue(first.isSuccess());

        ReservationResult second = reservationService.createReservation(
                1L,
                instance.getId(),
                3,
                "Second User",
                "second@email.com",
                "N/A",
                "None"
        );

        assertNotNull(second);
        assertFalse(second.isSuccess());
        assertEquals("That time slot is full.", second.getMessage());
    }

}
