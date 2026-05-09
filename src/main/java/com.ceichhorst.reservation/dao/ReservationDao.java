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
import java.util.ArrayList;
import java.util.Set;
import java.time.LocalDate;

/**
 * Data Access Object (DAO) for {@link Reservation} entities.
 *
 * <p>This class extends {@link GenericDao} and provides additional
 * domain-specific queries and operations related to reservations.</p>
 *
 * <p><strong>Concurrency:</strong> This DAO includes logic to safely create
 * reservations under concurrent access using pessimistic locking to prevent
 * overbooking of a {@link ServiceInstance}.</p>
 *
 * @author ceichhorst
 */
public class ReservationDao extends GenericDao<Reservation> {

    /**
     * Constructs a new reservation.
     */
    public ReservationDao() {
        super(Reservation.class);
    }

    /**
     * Attempts to create a reservation if sufficient capacity is available.
     * @param reservation the reservation to create; must include a valid service instance
     * @return {@code true} if the reservation was successfully created; {@code false} if capacity would be exceeded
     */
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
            .filter(Reservation::isActive)
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

    /**
     * Finds reservations by an exact match on customer name.
     * @param name the customer name to search for
     * @return a list of matching reservations
     */
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

    /**
     * Finds reservations by an exact match on email address.
     * @param email the email address to search for
     * @return a list of matching reservations
     */
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

    /**
     * Finds reservations by a filtered parameter
     */
    public List<Reservation> findByFilter(Long id, String customerName, String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> root = cq.from(Reservation.class);

        List<Predicate> predicates = new ArrayList<>();

        if (id != null) {
            predicates.add(cb.equal(root.get("id"), id));
        }

        if (customerName != null && customerName.trim().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("customerName")),
                    "%" + customerName.trim().toLowerCase() + "%"
            ));
        }

        if (email != null && email.trim().isEmpty()) {
            predicates.add(cb.like(
                    cb.lower(root.get("email")),
                    "%" + email.trim().toLowerCase() + "%"
            ));
        }

        if (!predicates.isEmpty()) {
            cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
        } else {
            cq.select(root);
        }

        List<Reservation> results = session.createQuery(cq).getResultList();
        session.close();

        return results;

    }

    /**
     * Counts the total number of reservations associated with a set of restaurants.
     * @param restaurantIds the set of restaurant IDs to include in the count
     * @return the total number of matching reservations
     */
    public Long countReservationsByService (Set<Long> restaurantIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Reservation> root = cq.from(Reservation.class);

        // Join: Reservation -> ServiceInstance -> Restaurant
        Join<Reservation, ServiceInstance> serviceJoin = root.join("serviceInstance");
        Join<ServiceInstance, Restaurant> restaurantJoin = serviceJoin.join("restaurant");

        Predicate restaurantPredicate = restaurantJoin.get("id").in(restaurantIds);

        Predicate activeReservationPredicate = root.get("status")
                        .in(
                                ReservationStatus.PENDING,
                                ReservationStatus.CONFIRMED
                        );

        cq.select(cb.count(root))
                .where(cb.and(
                        restaurantPredicate,
                        activeReservationPredicate
                ));

        Long count = session.createQuery(cq).getSingleResult();
        session.close();

        return count;

    }

    /**
     * Aggregates reservation statistics grouped by service date for given restaurants.
     * <p>This method returns a list of {@link ServiceReservationStats} projections,
     * each containing:</p>
     * <ul>
     *     <li>Service date</li>
     *     <li>Total number of reservations</li>
     *     <li>Total number of guests (sum of {@code partySize})</li>
     * </ul>
     *
     * <p>Results are grouped by {@code serviceDate} and ordered chronologically.</p>
     * @param restaurantIds the set of restaurant IDs to include
     * @return a list of aggregated reservation statistics
     */
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

        Predicate restaurantPredicate = restaurantJoin.get("id").in(restaurantIds);

        Predicate activeReservationPredicate = root.get("status")
                .in(
                        ReservationStatus.PENDING,
                        ReservationStatus.CONFIRMED
                );

        cq.where(cb.and(
                        restaurantPredicate,
                        activeReservationPredicate
                ));

        cq.groupBy(serviceJoin.get("serviceDate"));
        cq.orderBy(cb.asc(serviceJoin.get("serviceDate")));


        List<ServiceReservationStats> results = session.createQuery(cq).getResultList();
        session.close();

        return results;

    }

}
