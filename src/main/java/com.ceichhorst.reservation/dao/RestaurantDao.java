package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestaurantDao {

    private static final Logger logger = LogManager.getLogger(RestaurantDao.class);

    public List<Restaurant> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Restaurant", Restaurant.class).getResultList();
        } catch (Exception e) {
            logger.error("Failted to get restaurants", e);
            throw new RuntimeException(e);
        }

    }

    public void save(Restaurant restaurant) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(restaurant);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Failed to save restaurant", e);
            throw new RuntimeException(e);
        }
    }
}
