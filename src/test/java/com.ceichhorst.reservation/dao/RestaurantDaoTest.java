package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.util.HibernateUtil;
import com.ceichhorst.reservation.testutils.TestDatabase;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestaurantDaoTest {

    private RestaurantDao restaurantDao;

    @BeforeAll
    void setup() {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);

        restaurantDao = new RestaurantDao();
    }

    @BeforeEach
    void cleanDatabase() {
        TestDatabase.runSQL("cleandb.sql");
    }

    @Test
    void testGetByIdWithAdmins_returnsRestaurantWithInitializedAdmins() {

        Restaurant restaurant = restaurantDao.getByIdWithAdmins(1L);

        assertNotNull(restaurant);

        assertEquals(1L, restaurant.getId());
        assertEquals("Test Restaurant", restaurant.getName());

        assertNotNull(restaurant.getAdministrators());

        assertDoesNotThrow(() -> {
            restaurant.getAdministrators().size();
        });

        assertFalse(restaurant.getAdministrators().isEmpty());
    }
}
