package com.ceichhorst.reservation.controller;

import org.glassfish.jersey.jackson.JacksonFeature;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {

    public ApplicationConfig() {
        System.out.println("ApplicationConfig loaded");
    }

    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add(ReservationController.class);
        h.add(JacksonFeature.class);
        return h;
    }
}
