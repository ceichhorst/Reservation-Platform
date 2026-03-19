package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/reservations")
public class ReservationManagementServlet extends HttpServlet {

    private ReservationDao reservationDao = new ReservationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Reservation> reservations = reservationDao.getAll();

        request.setAttribute("reservations", reservations);

        request.getRequestDispatcher("/WEB-INF/admin/reservations.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long reservationId = Long.parseLong(request.getParameter("id"));
        String action = request.getParameter("action");

        Reservation reservation = reservationDao.getById(reservationId);

        if (reservation != null) {
            if ("confirm".equals(action)) {
                reservation.setStatus(ReservationStatus.CONFIRMED);
            } else if ("cancel".equals(action)) {
                reservation.setStatus(ReservationStatus.CANCELLED);
            }
            reservationDao.update(reservation);
        }

        response.sendRedirect("reservations");
    }
}
