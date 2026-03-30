package com.ceichhorst.reservation.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.entity.ReservationStatus;

import java.util.List;

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
