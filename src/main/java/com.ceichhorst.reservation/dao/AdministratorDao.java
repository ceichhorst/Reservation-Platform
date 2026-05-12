package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import com.ceichhorst.reservation.util.HibernateUtil;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Data Access Object (DAO) for {@link Administrator} entities.
 *
 * <p>This class extends {@link GenericDao} and provides additional query methods
 * for retrieving administrators based on unique attributes (such as username and email),
 * as well as accessing associated {@link Restaurant} entities.</p>
 *
 * <p>Queries are implemented using the JPA Criteria API for type safety and flexibility.</p>
 *
 * @author ceichhorst
 */
public class AdministratorDao extends GenericDao<Administrator> {

    /**
     * Constructs a new AdminstratorDao
     */
    public AdministratorDao() {
        super(Administrator.class);
    }

    // TODO do I really need this method anymore after using email now?
    /**
     * Retrieves an administrator by their username
     * @param username the username to search for
     * @return the matching administrator
     */
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

    /**
     * Retrieves an administrator by their email address.
     * @param email the email address to search for
     * @return the matching administrator
     */
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

    /**
     * Retrieves the IDs of restaurants managed by a specific administrator.
     * @param adminId the ID of the administrator
     * @return a set of restaurant IDs associated with the administrator
     */
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

    /**
     * Retrieves all {@link Restaurant} entities associated with a given administrator.
     * @param adminId the ID of the administrator
     * @return a list of restaurants managed by the administrator
     */
    public List<Restaurant> getRestaurantByAdminId(Long adminId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Restaurant> criteriaQuery = criteriaBuilder.createQuery(Restaurant.class);
            Root<Administrator> root = criteriaQuery.from(Administrator.class);

            Join<Administrator, Restaurant> restaurants = root.join("restaurants");

            criteriaQuery.select(restaurants)
                    .where(criteriaBuilder.equal(root.get("id"), adminId));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    /**
     * Adds a restaurant association to an administrator
     * @param adminId
     * @param restaurantId
     */
    public void addRestaurantAssociation(Long adminId, Long restaurantId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            Administrator admin = session.get(Administrator.class, adminId);
            Restaurant restaurant = session.get(Restaurant.class, restaurantId);

            if (admin == null || restaurant == null) {
                throw new IllegalArgumentException("Admin or restaurant not found");
            }

            Hibernate.initialize(admin.getRestaurants());

            admin.getRestaurants().add(restaurant);

            session.merge(admin);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException("Failed to add restaurant association", e);
        } finally {
            session.close();
        }
    }

    /**
     * Removes a restaurant association from an administrator
     * @param adminId
     * @param restaurantId
     */
    public void removeRestaurantAssociation(Long adminId, Long restaurantId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            Administrator admin = session.get(Administrator.class, adminId);
            Restaurant restaurant = session.get(Restaurant.class, restaurantId);

            if (admin == null || restaurant == null) {
                throw new IllegalArgumentException("Admin or restaurant not found");
            }

            Hibernate.initialize(admin.getRestaurants());

            admin.getRestaurants().remove(restaurant);

            session.merge(admin);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException("Failed to remove restaurant association", e);
        } finally {
            session.close();
        }
    }

}
