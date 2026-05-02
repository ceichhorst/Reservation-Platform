package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

// Core admin component for admins to manage reservations on admin pages
@WebServlet("/admin/reservations")
public class ReservationManagementServlet extends HttpServlet {

    private ReservationDao reservationDao = new ReservationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Is it best to make the auth check a called method compared to duplicating code?
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Reservation> reservations = reservationDao.getAll();

        request.setAttribute("reservations", reservations);

        request.getRequestDispatcher("/WEB-INF/admin/reservations.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

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

        response.sendRedirect(request.getContextPath() + "/admin/reservations");
    }
}
