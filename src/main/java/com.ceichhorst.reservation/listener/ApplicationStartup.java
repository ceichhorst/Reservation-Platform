package com.ceichhorst.reservation.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.ceichhorst.reservation.util.HibernateUtil;

@WebListener
public class ApplicationStartup implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(ApplicationStartup.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            HibernateUtil.setSessionFactory(sessionFactory);
            logger.info("Hibernate SessionFactory initialized successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Hibernate SessionFactory", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.shutdown();
        logger.info("Hibernate SessionFactory shut down");
    }
}
