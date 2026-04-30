package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import com.ceichhorst.reservation.util.HibernateUtil;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class AdministratorDao extends GenericDao<Administrator> {

    public AdministratorDao() {
        super(Administrator.class);
    }

    public Administrator getAdministratorByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Administrator> criteriaQuery = criteriaBuilder.createQuery(Administrator.class);
            Root<Administrator> root = criteriaQuery.from(Administrator.class);
            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(root.get("username"), username));

            Query<Administrator> query = session.createQuery(criteriaQuery);
            return query.uniqueResult();
        }
    }

    public Administrator getAdministratorByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Administrator> criteriaQuery = criteriaBuilder.createQuery(Administrator.class);
            Root<Administrator> root = criteriaQuery.from(Administrator.class);

            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(root.get("email"), email));

            return session.createQuery(criteriaQuery).uniqueResult();
        }
    }

    public List<Administrator> getAdministratorByRole(String role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Administrator> criteriaQuery = criteriaBuilder.createQuery(Administrator.class);
            Root<Administrator> root = criteriaQuery.from(Administrator.class);
            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(root.get("role"), role));

            Query<Administrator> query = session.createQuery(criteriaQuery);
            return query.getResultList();
        }
    }

    public Set<Long> getRestaurantIds(Long adminId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<Administrator> root = criteriaQuery.from(Administrator.class);

            Join<Object, Object> restaurants = root.join("restaurants");

            criteriaQuery.select(restaurants.get("id"))
                    .where(criteriaBuilder.equal(root.get("id"), adminId));

            return new HashSet<>(session.createQuery(criteriaQuery).getResultList());
        }
    }

    public List<Restaurant> getRestaurantByAdminId(Long adminId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Restaurant> criteriaQuery = criteriaBuilder.createQuery(Restaurant.class);
            Root<Administrator> root = criteriaQuery.from(Administrator.class);

            Join<Object, Object> restaurants = root.join("restaurants");

            criteriaQuery.select(restaurants.get("id"))
                    .where(criteriaBuilder.equal(root.get("id"), adminId));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

}
