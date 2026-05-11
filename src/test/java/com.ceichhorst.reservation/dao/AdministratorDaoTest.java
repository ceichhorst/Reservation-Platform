package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.testutils.TestDatabase;
import com.ceichhorst.reservation.util.HibernateUtil;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AdministratorDaoTest {

    private AdministratorDao adminDao;

    @BeforeAll
    void setup() throws Exception {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);
        adminDao = new AdministratorDao();
    }

    @BeforeEach
    void cleanDatabase() {
        TestDatabase.runSQL("cleandb.sql");
    }

    @Test
    void testGetAdministratorByEmail_existingEmail_returnsAdmin() {
        Administrator admin = adminDao.getAdministratorByEmail("seed@admin.com");
        assertNotNull(admin);
        assertEquals("seed@admin.com", admin.getEmail());
    }

    @Test
    void testGetAdministratorByEmail_nonExistentEmail_returnsNull() {
        Administrator admin = adminDao.getAdministratorByEmail("nobody@nowhere.com");
        assertNull(admin);
    }

    @Test
    void testGetRestaurantIds_validAdmin_returnIds() {
        Set<Long> ids = adminDao.getRestaurantIds(1L);
        assertNotNull(ids);
        assertFalse(ids.isEmpty());
        assertTrue(ids.contains(1L));
    }

    @Test
    void testGetRestaurantIds_unknownAdmin_returnsEmptySet() {
        Set<Long> ids = adminDao.getRestaurantIds(999L);
        assertNotNull(ids);
        assertTrue(ids.isEmpty());
    }

    @Test
    void testGetRestaurantByAdminId_returnsRestaurants() {
        List<Restaurant> restaurants = adminDao.getRestaurantByAdminId(1L);
        assertNotNull(restaurants);
        assertFalse(restaurants.isEmpty());
    }

    @Test
    void testGetById_existingAdmin_returnsAdmin() {
        Administrator admin = adminDao.getById(1L);
        assertNotNull(admin);
        assertEquals(1L, admin.getId());
    }

    @Test
    void testGetById_unknownId_returnsNull() {
        Administrator admin = adminDao.getById(999L);
        assertNull(admin);
    }
}
