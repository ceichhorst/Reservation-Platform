package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.ServiceInstance;
import com.ceichhorst.reservation.testutils.TestDatabase;
import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceInstanceDaoTest {

    private ServiceInstanceDao serviceDao;

    @BeforeAll
    void setup() throws Exception {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);
        serviceDao = new ServiceInstanceDao();
    }

    @BeforeEach
    void cleanDatabase() {
        TestDatabase.runSQL("cleandb.sql");
    }

    @Test
    void testGetByRestaurantId_returnUpcomingServicesForRestaurant() {

        List<ServiceInstance> services = serviceDao.getByRestaurantId(1L);

        assertNotNull(services);
        assertFalse(services.isEmpty());

        assertTrue(
                services.stream()
                        .allMatch(service ->
                                service.getRestaurant().getId().equals(1L))
        );

        assertTrue(
                services.stream()
                        .allMatch(service ->
                                !service.getServiceDate().isBefore(LocalDate.now()))
        );

    }

    @Test
    void testHasReservations_returnsTrueWhenReservationsExist() {

        boolean result = serviceDao.hasReservations(1000L);

        assertTrue(result);
    }

}
