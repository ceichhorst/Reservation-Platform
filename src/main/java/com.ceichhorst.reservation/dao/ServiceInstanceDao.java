package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Join;
import org.hibernate.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Data Access Object (DAO) for {@link ServiceInstance} entities.
 *
 * <p>This class extends {@link GenericDao} and provides query methods for
 * retrieving service instances based on date, restaurant, and reservation state.</p>
 *
 * <p>It uses the JPA Criteria API for type-safe query construction and supports
 * common filtering and lookup operations required for scheduling and availability.</p>
 *
 * @author ceichhorst
 */
public class ServiceInstanceDao extends GenericDao<ServiceInstance>{

    private static final Logger logger = LogManager.getLogger(ServiceInstanceDao.class);

    /**
     * Constructs a new ServiceInstanceDao
     */
    public ServiceInstanceDao() {
        super(ServiceInstance.class);
    }

    // TODO do I really need this method?
    /**
     * Retrieves all upcoming service instances from today onward.
     * @return a list of upcoming service instances
     */
    public List<ServiceInstance> getUpcomingServices() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb =session.getCriteriaBuilder();
            CriteriaQuery<ServiceInstance> cq = cb.createQuery(ServiceInstance.class);
            Root<ServiceInstance> root = cq.from(ServiceInstance.class);

            // Filters for future dates
            cq.select(root)
                    .where(cb.greaterThanOrEqualTo(root.get("serviceDate"), LocalDate.now()))
                    .orderBy(
                            cb.asc(root.get("serviceDate")),
                            cb.asc(root.get("serviceTime"))
                    );

            return session.createQuery(cq).getResultList();
        }
    }

    /**
     * Retrieves all service instances for a given restaurant.
     * @param restaurantId the ID of the restaurant
     * @return a list of service instances for the specified restaurant
     */
    public List<ServiceInstance> getByRestaurantId(Long restaurantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb =session.getCriteriaBuilder();
            CriteriaQuery<ServiceInstance> cq = cb.createQuery(ServiceInstance.class);
            Root<ServiceInstance> root = cq.from(ServiceInstance.class);

            root.fetch("reservations", JoinType.LEFT);

            Predicate restaurantPredicate = cb.equal
                    (root.get("restaurant").get("id"), restaurantId);

            Predicate upcomingPredicate = cb.greaterThanOrEqualTo(
                    root.get("serviceDate"), LocalDate.now()
            );

            // Filters out previous dates
            cq.select(root)
                    .distinct(true)
                    .where(cb.and(restaurantPredicate, upcomingPredicate))
                    .orderBy(
                            cb.asc(root.get("serviceDate")),
                            cb.asc(root.get("serviceTime"))
                    );

            return session.createQuery(cq).getResultList();
        }
    }

    // TODO do I need this method? - think about 'future' filtering here
    /**
     * Retrieves all service instances for a specific date.
     * @param date the service date to filter by
     * @return a list of service instances occurring on the specified date
     */
    public List<ServiceInstance> getByDate(LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb =session.getCriteriaBuilder();
            CriteriaQuery<ServiceInstance> cq = cb.createQuery(ServiceInstance.class);
            Root<ServiceInstance> root = cq.from(ServiceInstance.class);

            // Filters for future dates
            cq.select(root)
                    .where(cb.equal(root.get("serviceDate"), date))
                    .orderBy(cb.asc(root.get("serviceTime")));

            return session.createQuery(cq).getResultList();
        }
    }

    /**
     * Retrieves service instances for a specific restaurant on a given date.
     * @param restaurantId the ID of the restaurant
     * @param date date the service date
     * @return a list of matching service instances
     */
    public List<ServiceInstance> getServicesByRestaurantOnDate(Long restaurantId, LocalDate date) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<ServiceInstance> services = new ArrayList<>();

        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ServiceInstance> cq = cb.createQuery(ServiceInstance.class);

            Root<ServiceInstance> root = cq.from(ServiceInstance.class);

            Predicate restaurantPredicate = cb.equal(root.get("restaurant").get("id"), restaurantId);
            Predicate datePredicate = cb.equal(root.get("serviceDate"), date);

            cq.select(root)
                    .where(cb.and(restaurantPredicate, datePredicate))
                    .orderBy(cb.asc(root.get("serviceTime")));

            services = session.createQuery(cq).getResultList();
        } catch (Exception e) {
            logger.error("Error fetching services for restaurant {} on date {}", restaurantId, date, e);
        } finally {
            session.close();
        }

        return services;
    }

    /**
     * Retrieves service instances belonging to any of the specified restaurants.
     * @param restaurantIds the set of restaurant IDs to include
     * @return a list of matching service instances
     */
    public List<ServiceInstance> getServicesByRestaurants(Set<Long> restaurantIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ServiceInstance> cq = cb.createQuery(ServiceInstance.class);

        Root<ServiceInstance> root = cq.from(ServiceInstance.class);

        cq.select(root)
                .where(root.get("restaurant").get("id").in(restaurantIds));

        List<ServiceInstance> results = session.createQuery(cq).getResultList();

        return results;
    }

    /**
     * Determines whether a service instance has any associated reservations.
     * @param serviceId the ID of the service instance
     * @return {@code true} if at least one reservation exists; {@code false} otherwise
     */
    public boolean hasReservations(Long serviceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);

            Root<ServiceInstance> root = cq.from(ServiceInstance.class);

            Join<Object, Object> reservations = root.join("reservations", JoinType.INNER);

            cq.select(cb.count(reservations));
            cq.where(cb.equal(root.get("id"), serviceId));

            Long count = session.createQuery(cq).getSingleResult();

            return count != null && count > 0;
        }
    }

}
