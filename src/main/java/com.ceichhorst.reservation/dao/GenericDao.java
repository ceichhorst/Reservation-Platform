package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Generic DAO providing basic CRUD operations for entities.
 *
 * <p>This class abstracts common persistence operations using Hibernate and can be
 * extended or instantiated for any entity type.</p>
 *
 * <p>Each method manages its own {@link Session} and {@link Transaction}, making
 * this implementation simple but less suitable for complex transactional workflows
 * that span multiple operations.</p>
 *
 * @author ceichhorst
 */
public class GenericDao<T> {

    /**
     * The entity class type managed by this DAO.
     */
    protected Class<T> type;

    /**
     * Constructs a new GenericDao for the specified entity type.
     * @param type the class of the entity
     */
    public GenericDao(Class<T> type) {
        this.type = type;
    }

    /**
     * Retrieves an entity by its primary key.
     * @param id the primary key of the entity
     * @return the entity instance
     */
    public T getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T entity = session.get(type, id);

        session.close();
        return entity;
    }

    /**
     * Retrieves all instances of an entity type.
     * @return a list of all the entities
     */
    public List<T> getAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);
        Root<T> root = cq.from(type);
        cq.select(root);

        List<T> results = session.createQuery(cq).getResultList();
        session.close();
        return results;

    }

    /**
     * Persists a new entity to the database.
     * @param entity the entity to persist
     */
    public void save(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.persist(entity);

        tx.commit();
        session.close();
    }

    /**
     * Updates an existing entity in the database.
     * @param entity the entity to update
     */
    public void update(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.merge(entity);

        tx.commit();
        session.close();
    }

    /**
     * Deletes an existing entity in the database.
     * @param entity the entity to delete
     */
    public void delete(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.remove(entity);

        tx.commit();
        session.close();
    }
}
