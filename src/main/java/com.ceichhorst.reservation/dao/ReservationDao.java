package com.ceichhorst.reservation.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.LockMode;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class ReservationDao extends GenericDaoImpl<Reservation> {

    public ReservationDao() {
        super(Reservation.class);
    }

    public boolean createReservationIfAvailable(int serviceId, int partySize, String customerName, String email) {

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
        }
    }

    /**
     * Admin side methods - add tests to ReservationDaoTest
     *
     */

    public List<Reservation> getByStatus(ReservationStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Reservation r WHERE r.status = :status", Reservation.class).setParameter("status", status)
                    .getResultList();
        }
    }

    public List<Reservation> findByCustomerName(String name) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
            Root<Reservation> root = cq.from(Reservation.class);

            cq.select(root)
                    .where(cb.equal(root.get("customerName"), name));

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Reservation> findByEmail(String email) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
            Root<Reservation> root = cq.from(Reservation.class);

            cq.select(root)
                    .where(cb.equal(root.get("email"), email));

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

}