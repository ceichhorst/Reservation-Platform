package com.ceichhorst.reservation.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.ceichhorst.reservation.util.HibernateUtil;

@WebListener
public class ApplicationStartup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            HibernateUtil.setSessionFactory(sessionFactory);
            System.out.println("Hibernate SessionFactory initialized successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Hibernate SessionFactory", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
        System.out.println("Hibernate SessionFactory shut down");
    }
}
