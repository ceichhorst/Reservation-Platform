package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationAction;
import com.ceichhorst.reservation.entity.ReservationActionType;
import com.ceichhorst.reservation.util.HibernateUtil;
import com.ceichhorst.reservation.testutils.TestDatabase;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationActionDaoTest {

    private ReservationActionDao actionDao;
    private ReservationDao reservationDao;
    private AdministratorDao adminDao;

    @BeforeAll
    void setup() {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        HibernateUtil.setSessionFactory(sessionFactory);

        actionDao = new ReservationActionDao();
        reservationDao = new ReservationDao();
        adminDao = new AdministratorDao();
    }

    @BeforeEach
    void cleanDatabase() {
        TestDatabase.runSQL("cleandb.sql");
    }

    @Test
    void testRecord_persistsReservationAction() {

        Reservation reservation = reservationDao.getById(2000L);
        Administrator admin = adminDao.getById(1L);

        actionDao.record(
                reservation,
                admin,
                ReservationActionType.CONFIRMED
        );

        List<ReservationAction> actions =
                actionDao.getByReservationId(2000L);

        assertNotNull(actions);
        assertEquals(1, actions.size());

        ReservationAction action = actions.get(0);

        assertEquals(
                ReservationActionType.CONFIRMED,
                action.getAction()
        );

        assertEquals(
                2000L,
                ReservationActionType.CONFIRMED
        );

        assertEquals(
                1L,
                action.getAdmin().getId()
        );

        assertNotNull(action.getActionTime());
    }


}
