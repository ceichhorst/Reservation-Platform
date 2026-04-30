package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;
import com.ceichhorst.reservation.service.ServiceReservationStats;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.LockMode;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Join;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;

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

    public Long countReservationsByService (Set<Long> restaurantIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Reservation> root = cq.from(Reservation.class);

        // Join: Reservation -> ServiceInstance -> Restaurant
        Join<Reservation, ServiceInstance> serviceJoin = root.join("serviceInstance");
        Join<ServiceInstance, Restaurant> restaurantJoin = serviceJoin.join("restaurant");

        cq.select(cb.count(root))
                .where(restaurantJoin.get("id").in(restaurantIds));

        Long count = session.createQuery(cq).getSingleResult();
        session.close();

        return count;

    }

    public List<ServiceReservationStats> countReservationsGroupedByService (Set<Long> restaurantIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ServiceReservationStats> cq = cb.createQuery(ServiceReservationStats.class);
        Root<Reservation> root = cq.from(Reservation.class);

        // Join: Reservation -> ServiceInstance -> Restaurant
        Join<Reservation, ServiceInstance> serviceJoin = root.join("serviceInstance");
        Join<ServiceInstance, Restaurant> restaurantJoin = serviceJoin.join("restaurant");

        cq.select(cb.construct(
                ServiceReservationStats.class,
                serviceJoin.get("serviceDate"),
                cb.count(root),
                cb.sum(root.get("partySize")).as(Long.class)
        ));

        cq.where(restaurantJoin.get("id").in(restaurantIds));
        cq.groupBy(serviceJoin.get("serviceDate"));
        cq.orderBy(cb.asc(serviceJoin.get("serviceDate")));


        List<ServiceReservationStats> results = session.createQuery(cq).getResultList();
        session.close();

        return results;

    }


}
