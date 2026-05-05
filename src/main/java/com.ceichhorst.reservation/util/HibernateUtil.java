package com.ceichhorst.reservation.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;

/**
 * Utility class for managing the Hibernate {@link SessionFactory}.
 *
 * <p>This class provides a globally accessible reference to a single
 * {@link SessionFactory} instance used throughout the application.</p>
 *
 * <p><strong>Lifecycle:</strong></p>
 * <ul>
 *   <li>The {@link SessionFactory} must be initialized externally via {@link #setSessionFactory(SessionFactory)}</li>
 *   <li>Once set, it can be accessed using {@link #getSessionFactory()}</li>
 *   <li>The factory should be properly closed during application shutdown using {@link #shutdown()}</li>
 * </ul>
 *
 * @author ceichhorst
 */
public class HibernateUtil {

    /**
     * The single {@link SessionFactory} instance.
     */
    private static SessionFactory sessionFactory;

    /**
     * Retrieves the configured {@link SessionFactory}.
     * @return the active session factory
     * @throws IllegalStateException if the session factory has not been initialized
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException(
                    "SessionFactory not set.");
        }
        return sessionFactory;
    }

    /**
     * Sets the {@link SessionFactory} instance.
     * @param sessionFactory the session factory to use
     */
    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateUtil.sessionFactory = sessionFactory;
    }


    /**
     * Shuts down the Hibernate {@link SessionFactory}.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}