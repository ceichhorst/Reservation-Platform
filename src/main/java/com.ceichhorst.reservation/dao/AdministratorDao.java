package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Administrator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.ceichhorst.reservation.util.HibernateUtil;

import java.util.List;

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

}
