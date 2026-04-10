package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Restaurant;

public class RestaurantDao extends GenericDao<Restaurant> {

    public RestaurantDao() {
        super(Restaurant.class);
    }
}
