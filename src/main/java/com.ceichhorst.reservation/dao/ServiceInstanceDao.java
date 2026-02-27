package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ServiceInstanceDao {

    private static final Logger logger = LogManager.getLogger(ServiceInstanceDao.class);

    public List<ServiceInstance> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM ServiceInstance", ServiceInstance.class)
                    .getResultList();
        } catch (HibernateException e) {
            logger.error("Failed to fetch all ServiceInstance records", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Unexpected error fetching ServiceInstance records", e);
            throw new RuntimeException(e);
        }
    }
}
