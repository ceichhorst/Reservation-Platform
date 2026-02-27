package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class RestaurantDao {

    public List<Restaurant> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Restaurant", Restaurant.class).getResultList();
        }
    }
}
