package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class GenericDao<T> {

    protected Class<T> type;

    public GenericDao(Class<T> type) {
        this.type = type;
    }

    public T getById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T entity = session.get(type, id);

        session.close();
        return entity;
    }

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

    public void save(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.persist(entity);

        tx.commit();
        session.close();
    }

    public void update(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.merge(entity);

        tx.commit();
        session.close();
    }

    public void delete(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.remove(entity);

        tx.commit();
        session.close();
    }
}
