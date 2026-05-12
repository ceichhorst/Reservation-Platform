package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.util.HibernateUtil;

import org.hibernate.Session;

/**
 * Data Access Object (DAO) for {@link Restaurant} entities.
 *
 * <p>This class extends {@link GenericDao} to provide basic CRUD operations
 * for {@link Restaurant} instances.</p>
 *
 * @author ceichhorst
 */
public class RestaurantDao extends GenericDao<Restaurant> {

    /**
     * Constructs a new RestaurantDao
     */
    public RestaurantDao() {
        super(Restaurant.class);
    }

    /**
     * Retrieves a selected restaurant alongside its associated admin(s)
     * @param id the id of the restaurant
     * @return the restaurant alongside its associated admin(s)
     */
    public Restaurant getByIdWithAdmins(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Restaurant restaurant = session.get(Restaurant.class, id);
            if (restaurant != null) {
                restaurant.getAdministrators().size();
            }
            return restaurant;
        }
    }
}
