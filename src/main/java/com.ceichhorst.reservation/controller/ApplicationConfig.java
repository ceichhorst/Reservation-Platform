package com.ceichhorst.reservation.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;

/**
 * Application config for ReservationController API
 *
 * @author ceichhorst
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {

    private static final Logger logger = LogManager.getLogger(ApplicationConfig.class);

    public ApplicationConfig() {
        logger.info("ApplicationConfig loaded");
    }

    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add(AvailabilityController.class);
        h.add(ReservationController.class);
        h.add(JacksonFeature.class);
        return h;
    }
}
