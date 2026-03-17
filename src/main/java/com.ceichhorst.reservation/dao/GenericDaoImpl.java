package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class GenericDaoImpl<T> implements GenericDao<T> {

    private static final Logger logger = LogManager.getLogger(GenericDaoImpl.class);

    private final Class<T> type;

    protected GenericDaoImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public T getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(type, id);
        } catch (HibernateException e) {
            logger.error("Failed to get entity by id={}", id, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(type);
            Root<T> root = cq.from(type);
            cq.select(root);
            return session.createQuery(cq).getResultList();
        } catch (HibernateException e) {
            logger.error("Failed to get all entities of type {}", type.getSimpleName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            logger.info("{} saved successfully", type.getSimpleName());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Failed to save entity of type {}", type.getSimpleName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            logger.info("{} updated successfully", type.getSimpleName());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Failed to update entity of type {}", type.getSimpleName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();
            logger.info("{} deleted successfully", type.getSimpleName());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Failed to delete entity of type {}", type.getSimpleName(), e);
            throw new RuntimeException(e);
        }
    }
}
