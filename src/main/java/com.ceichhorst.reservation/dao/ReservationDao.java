package com.ceichhorst.reservation.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.LockMode;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;

import java.util.List;

public class ReservationDao {

    private static final Logger logger =
            LogManager.getLogger(ReservationDao.class);

    public void save (Reservation reservation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(reservation);
            tx.commit();
            logger.info("Reservation saved successfully for id={}", reservation.getId());
        } catch (Exception e) {
            logger.error("Failed to save reservation", e);
            throw new RuntimeException(e);
        }
    }

    public Reservation getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Reservation.class, id);
        } catch (Exception e) {
            logger.error("Failed to get reservation by ID: " + id, e);
            throw new RuntimeException(e);
        }
    }

    public void update(Reservation reservation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            session.merge(reservation);

            tx.commit();
            logger.info("Reservation updated successfully for idk={}", reservation.getId());
        } catch (Exception e) {
            logger.error("Failed to updated reservation", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(Reservation reservation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(reservation);
            tx.commit();
        } catch (Exception e) {
            logger.error("Failed to delete reservation", e);
            throw new RuntimeException(e);
        }
    }

    public boolean createReservationIfAvailable(int serviceId, int partySize, String customerName, String email) {

        logger.info("Creating reservation...");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            ServiceInstance service = session.get(ServiceInstance.class, serviceId, LockMode.PESSIMISTIC_WRITE);

            int currentBooked = service.getReservations()
                    .stream()
                    .mapToInt(Reservation::getPartySize)
                    .sum();

            if (currentBooked + partySize > service.getCapacity()) {
                tx.rollback();
                return false;
            }

            Reservation reservation = new Reservation();
            reservation.setCustomerName(customerName);
            reservation.setEmail(email);
            reservation.setPartySize(partySize);
            reservation.setServiceInstance(service);

            session.persist(reservation);
            tx.commit();
            return true;
        } catch (Exception e) {
            logger.error("Failed to create reservation for serviceId=" + serviceId, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Admin side methods - add tests to ReservationDaoTest
     *
     */

    public List<Reservation> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Reservation", Reservation.class).getResultList();
        } catch (Exception e) {
            logger.error("Failed to retrieve all reservations", e);
            throw new RuntimeException(e);
        }
    }

    public List<Reservation> getByStatus(ReservationStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Reservation r WHERE r.status = :status", Reservation.class).setParameter("status", status)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Failed to retrieve reservations by status", e);
            throw new RuntimeException(e);
        }
    }

    // TODO the 'try' block in all of the methods is pretty much duplicated throughout this class. Consider finding a way to minimize duplicate code here.
}