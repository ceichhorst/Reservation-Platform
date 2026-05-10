package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.ReservationAction;
import com.ceichhorst.reservation.entity.ReservationActionType;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * DAO for persisting and retrieving {@link ReservationAction} audit records
 *
 * @author ceichhorst
 */
public class ReservationActionDao {

    /**
     * Records an administrative action taken on a reservation
     * @param reservation
     * @param admin
     * @param actionType
     */
    public void record(Reservation reservation, Administrator admin, ReservationActionType actionType) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            ReservationAction action = new ReservationAction();
            action.setReservation(reservation);
            action.setAdmin(admin);
            action.setAction(actionType);

            session.persist(action);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

    }

    /**
     * Retrieves the full action history for a given reservation, ordered chronologically
     * @param reservationId
     * @return list of actions taken on reservations
     */
    public List<ReservationAction> getByReservationId(Long reservationId) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb =session.getCriteriaBuilder();
        CriteriaQuery<ReservationAction> cq = cb.createQuery(ReservationAction.class);
        Root<ReservationAction> root = cq.from(ReservationAction.class);

        cq.select(root)
                .where(cb.equal(root.get("reservation").get("id"), reservationId))
                .orderBy(cb.asc(root.get("actionTime")));

        List<ReservationAction> results = session.createQuery(cq).getResultList();

        return results;
    }
}
