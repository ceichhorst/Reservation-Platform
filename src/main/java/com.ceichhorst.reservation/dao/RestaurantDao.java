package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.Session;
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
        }

    }
}
