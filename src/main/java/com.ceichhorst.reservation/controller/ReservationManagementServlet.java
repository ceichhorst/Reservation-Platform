package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.service.ServiceInstance;

import com.ceichhorst.reservation.service.ServiceTimeFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Core admin component for admins to manage reservations on admin pages
/**
 * Servlet responsible for administrative management of customer reservations.
 *
 * <p>This servlet allows administrators to view all reservations and update
 * their status (e.g., confirm or cancel bookings) through the admin interface.</p>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Validate administrator authentication session</li>
 *   <li>Display all reservations in the system</li>
 *   <li>Allow status updates on individual reservations</li>
 *   <li>Persist changes to reservation state</li>
 * </ul>
 *
 * <p><strong>Supported actions:</strong></p>
 * <ul>
 *   <li><strong>GET:</strong> Retrieve and display all reservations</li>
 *   <li><strong>POST:</strong> Update reservation status (confirm or cancel)</li>
 * </ul>
 *
 * <p>This servlet interacts directly with {@link ReservationDao} for data access
 * and uses {@link ReservationStatus} to manage reservation state transitions.</p>
 *
 * @author ceichhorst
 */
@WebServlet("/admin/reservations")
public class ReservationManagementServlet extends HttpServlet {

    /**
     * Data access object for managing {@link Reservation} entities.
     */
    private ReservationDao reservationDao = new ReservationDao();

    /**
     * Handles HTTP GET requests to display all reservations.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates that the user is authenticated</li>
     *   <li>Retrieves all reservations from the database</li>
     *   <li>Forwards the data to the admin reservations view</li>
     * </ol>
     *
     * <p><strong>Request attributes set:</strong></p>
     * <ul>
     *   <li>{@code reservations} → list of all {@link Reservation}</li>
     * </ul>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if request forwarding fails
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO Is it best to make the auth check a called method compared to duplicating code?
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Filter params
        String idParam = request.getParameter("id");
        String customerName = request.getParameter("customerName");
        String email = request.getParameter("email");
        String dateParam = request.getParameter("serviceDate");

        Long id = null;
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                id = Long.parseLong(idParam.trim());
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Confirmation ID must be a number.");
            }
        }

        LocalDate serviceDate = null;
        if (dateParam != null && !dateParam.trim().isEmpty()) {
            try {
                serviceDate = LocalDate.parse(dateParam.trim());
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid date format.");
            }
        }

        List<Reservation> reservations = reservationDao.findByFilter(id ,customerName, email, serviceDate);

        ServiceTimeFormatter formatter = new ServiceTimeFormatter();
        List<ServiceInstance> serviceInstances = reservations.stream()
                        .map(Reservation::getServiceInstance)
                                .collect(Collectors.toList());
        formatter.formatTimes(serviceInstances);

        request.setAttribute("reservations", reservations);
        request.setAttribute("filterId", idParam != null ? idParam : "");
        request.setAttribute("filterCustomerName", customerName != null ? customerName : "");
        request.setAttribute("filterEmail", email != null ? email : "");
        request.setAttribute("filterDate", dateParam != null ? dateParam : "");

        request.getRequestDispatcher("/WEB-INF/admin/reservations.jsp")
                .forward(request, response);
    }

    /**
     * Handles HTTP POST requests to update reservation status.
     *
     * <p>This method allows administrators to change the state of a reservation
     * to either confirmed or cancelled.</p>
     *
     * <p><strong>Supported actions:</strong></p>
     * <ul>
     *   <li>{@code confirm} → sets status to {@link ReservationStatus#CONFIRMED}</li>
     *   <li>{@code cancel} → sets status to {@link ReservationStatus#CANCELLED}</li>
     * </ul>
     *
     * <p><strong>Request parameters:</strong></p>
     * <ul>
     *   <li>{@code id} → reservation ID</li>
     *   <li>{@code action} → action to perform (confirm or cancel)</li>
     * </ul>
     *
     * @param request the HTTP request containing reservation ID and action
     * @param response the HTTP response
     * @throws ServletException if processing fails
     * @throws IOException if an I/O error occurs
     */
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
