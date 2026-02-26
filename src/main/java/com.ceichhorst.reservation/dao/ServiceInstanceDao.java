package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class ServiceInstanceDao {

    public List<ServiceInstance> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM service_instance", ServiceInstance.class)
                    .getResultList();
        }
    }
}
