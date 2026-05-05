package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Restaurant;

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
     * Constructs a new ReservationDao
     */
    public RestaurantDao() {
        super(Restaurant.class);
    }
}
