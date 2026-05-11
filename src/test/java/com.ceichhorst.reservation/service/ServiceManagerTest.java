package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.entity.SchedulingType;
import com.ceichhorst.reservation.testutils.TestDatabase;
import com.ceichhorst.reservation.util.HibernateUtil;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceManagerTest {

    private ServiceManager serviceManager;
    private ServiceInstanceDao serviceDao;

    @BeforeAll
    void setup() throws Exception {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate-test-cfg.xml")
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);
        serviceManager = new ServiceManager();
        serviceDao = new ServiceInstanceDao();
    }

    @BeforeEach
    void cleanDatabase() {
        TestDatabase.runSQL("cleandb.sql");
    }

    @Test
    void testAddService_dateOnly_createsSingleService() {
        serviceManager.addService(
                1L, 1L,
                LocalDate.now().plusDays(1),
                LocalTime.MIDNIGHT, null, 20
        );

        List<ServiceInstance> services = serviceDao.getByRestaurantId(1L);
        assertTrue(services.stream()
                .anyMatch(s -> s.getServiceDate().equals(LocalDate.now().plusDays(1))));
    }

    @Test
    void testAddService_dateTime_createsMultipleSlots() {
        serviceManager.updateSchedulingType(1L, 1L, "DATE_TIME");
        LocalTime start = LocalTime.of(16, 0);
        LocalTime end = LocalTime.of(17,0);

        serviceManager.addService(1L, 1L, LocalDate.now().plusDays(2), start, end, 10);

        List<ServiceInstance> services = serviceDao.getByRestaurantId(1L);
        long slotsForDate = services.stream()
                .filter(s -> s.getServiceDate().equals(LocalDate.now().plusDays(2)))
                .count();

        assertEquals(4, slotsForDate);
    }

    @Test
    void testAddService_unauthorizedAdmin_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                serviceManager.addService(
                        999L, 1L,
                        LocalDate.now().plusDays(1),
                        LocalTime.MIDNIGHT, null, 20
                )
        );
    }

    @Test
    void testDeleteService_withNoReservations_succeeds() {
        List<ServiceInstance> services = serviceDao.getByRestaurantId(1L);
        assertFalse(services.isEmpty());

        ServiceInstance target = services.get(0);
        serviceManager.deleteService(1L, target.getId());

        ServiceInstance deleted = serviceDao.getById(target.getId());
        assertNull();
    }

    @Test
    void testToggleVisibility_togglesToFalse() {
        List<ServiceInstance> services = serviceDao.getByRestaurantId(1L);
        ServiceInstance service = services.get(0);
        boolean original = service.getVisible();

        serviceManager.toggleVisibility(1L, service.getId());

        ServiceInstance updated = serviceDao.getById(service.getId());
        assertEquals(!original, updated.getVisible());
    }

    @Test
    void testToggleVisibility_togglesTwiceRestoresOriginal() {
        List<ServiceInstance> services = serviceDao.getByRestaurantId(1L);
        ServiceInstance service = services.get(0);
        boolean original = service.getVisible();

        serviceManager.toggleVisibility(1L, service.getId());
        serviceManager.toggleVisibility(1L, service.getId());

        ServiceInstance updated = serviceDao.getById(service.getId());
        assertEquals(original, updated.getVisible());
    }

    @Test
    void testUpdateSchedulingType_updatesSuccessfully() {
        serviceManager.updateSchedulingType(1L, 1L, "FIXED_TIME_SLOTS");

        assertDoesNotThrow(() ->
                serviceManager.addService(
                        1L, 1L,
                        LocalDate.now().plusDays(3),
                        LocalTime.of(19, 0), null, 15
                )
        );
    }

    @Test
    void testAuthorizeAdminAccess_validAdmin_doesNotThrow() {
        assertDoesNotThrow(() -> serviceManager.authorizeAdminAccess(1L, 1L));
    }

    @Test
    void testAuthorizeAdminAccess_invalidAdmin_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                serviceManager.authorizeAdminAccess(999L, 1L));
    }

}
