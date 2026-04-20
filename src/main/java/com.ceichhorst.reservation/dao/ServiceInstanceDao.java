package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class ServiceInstanceDao extends GenericDao<ServiceInstance>{
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

            // Filters for future dates
            cq.select(root)
                    .where(cb.equal(root.get("restaurant").get("id"), restaurantId));

            return session.createQuery(cq).getResultList();
        }
    }
}
