package com.ceichhorst.reservation.dao;

import java.util.List;

public interface GenericDao<T> {
    T getById(Long id);
    List<T> getAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
}
