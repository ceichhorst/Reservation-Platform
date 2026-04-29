package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceInstance;
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

public class ServiceInstanceDao extends GenericDao<ServiceInstance>{

    private static final Logger logger = LogManager.getLogger(ServiceInstanceDao.class);

    public ServiceInstanceDao() {
        super(ServiceInstance.class);
    }

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

    public List<ServiceInstance> getByRestaurantId(Long restaurantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb =session.getCriteriaBuilder();
            CriteriaQuery<ServiceInstance> cq = cb.createQuery(ServiceInstance.class);
            Root<ServiceInstance> root = cq.from(ServiceInstance.class);

            root.fetch("reservations", JoinType.LEFT);

            // Filters for future dates
            cq.select(root)
                    .distinct(true)
                    .where(cb.equal(root.get("restaurant").get("id"), restaurantId));

            return session.createQuery(cq).getResultList();
        }
    }

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

    public List<ServiceInstance> getServicesByRestaurants(Set<Long> restaurantIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ServiceInstance> cq = cb.createQuery(ServiceInstance.class);

        Root<ServiceInstance> root = cq.from(ServiceInstance.class);

        Join<ServiceInstance, Restaurant> restaurantJoin = root.join("restaurant");

        cq.select(root)
                .where(root.get("restaurant").get("id").in(restaurantIds));

        List<ServiceInstance> results = session.createQuery(cq).getResultList();

        return results;
    }

}
