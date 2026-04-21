package com.ceichhorst.reservation.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.QueryParam;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.entity.ReservationStatus;

import java.util.List;

// API / REST layer to access data
@Path("/reservation")
public class ReservationController {

    private final ReservationDao reservationDao = new ReservationDao();

    public ReservationController() {
        System.out.println("ReservationController loaded");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservations(
            @QueryParam("id") Long id,
            @QueryParam("customerName") String customerName,
            @QueryParam("email") String email) {

        // Return one reservation by ID
        if (id != null) {
            Reservation reservation = reservationDao.getById(id);
            if (reservation != null) {
                return Response.ok(reservation).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Reservation not found for ID: " + id)
                        .build();
            }
        }

        // Return filtered or all reservations
        List<Reservation> reservations;
        if (customerName != null) {
            reservations = reservationDao.findByCustomerName(customerName);
        } else if (email != null) {
            reservations = reservationDao.findByEmail(email);
        } else {
            reservations = reservationDao.getAll();
        }

        return Response.ok(reservations).build();
    }
}
