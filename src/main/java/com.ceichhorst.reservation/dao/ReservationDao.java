package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.LockMode;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

public class ReservationDao extends GenericDao<Reservation> {

    public ReservationDao() {
        super(Reservation.class);
    }

    public boolean createReservationIfAvailable(Reservation reservation) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        ServiceInstance service = session.get(
                ServiceInstance.class,
                reservation.getServiceInstance().getId(),
                LockMode.PESSIMISTIC_WRITE
        );

        int currentBooked = service.getReservations()
            .stream()
            .mapToInt(Reservation::getPartySize)
                .sum();

        if (currentBooked + reservation.getPartySize() > service.getCapacity()) {
            tx.rollback();
            session.close();
            return false;
        }

        reservation.setServiceInstance(service);

        session.persist(reservation);

        tx.commit();
        session.close();

        return true;

    }

    List<Reservation> getByStatus(ReservationStatus status) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> root = cq.from(Reservation.class);

        cq.select(root)
                .where(cb.equal(root.get("status"), status));

        List<Reservation> results = session.createQuery(cq).getResultList();
        session.close();

        return results;
    }

    public List<Reservation> findByCustomerName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> root = cq.from(Reservation.class);

        cq.select(root)
                .where(cb.equal(root.get("customerName"), name));

        List<Reservation> results = session.createQuery(cq).getResultList();
        session.close();

        return results;
    }

    public List<Reservation> findByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> root = cq.from(Reservation.class);

        cq.select(root)
                .where(cb.equal(root.get("email"), email));

        List<Reservation> results = session.createQuery(cq).getResultList();
        session.close();

        return results;
    }
}
