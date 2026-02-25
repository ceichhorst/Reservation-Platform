package com.ceichhorst.reservation.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.LockMode;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.util.HibernateUtil;

public class ReservationDao {

    private static final Logger logger =
            LogManager.getLogger(Reservation.class);

    public void save (Reservation reservation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(reservation);
            tx.commit();
        }
    }

    public Reservation getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Reservation.class, id);
        }
    }

    public void delete(Reservation reservation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(reservation);
            tx.commit();
        }
    }

    public boolean createReservationIfAvailable(int serviceId, int partySize, String name) {

        logger.info("Creating reservation...");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Service service = session.get(Service.class, serviceId, LockMode.PESSIMISTIC_WRITE);

            int currentBooked = service.getReservations()
                    .stream()
                    .mapToInt(Reservation::getPartySize())
                    .sum();

            if (currentBooked + partySize > service.getCapacity()) {
                tx.rollback();
                return false;
            }

            Reservation reservation = new Reservation();
            reservation.setCustomerName(name);
            reservation.setPartySize(partySize);
            reservation.setService(service);

            session.persist(reservation);
            tx.commit();
            return true;
        }
    }
}