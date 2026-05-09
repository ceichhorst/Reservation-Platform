package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationActionDao;
import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.entity.*;
import com.ceichhorst.reservation.service.ServiceInstance;

import com.ceichhorst.reservation.service.ServiceTimeFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * Dao
     */
    private ReservationActionDao actionDao = new ReservationActionDao();

    /**
     * Dao
     */
    private AdministratorDao adminDao = new AdministratorDao();

    /**
     * Formatting service time
     */
    private ServiceTimeFormatter formatter = new ServiceTimeFormatter();

    /**
     * Logger for logging error
     */
    private static final Logger logger = LogManager.getLogger(ReservationManagementServlet.class);

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

        List<ServiceInstance> serviceInstances = reservations.stream()
                        .map(Reservation::getServiceInstance)
                                .collect(Collectors.toList());
        formatter.formatTimes(serviceInstances);

        String editIdParam = request.getParameter("editId");
        if (editIdParam != null && !editIdParam.trim().isEmpty()) {
            try {
                Long editId = Long.parseLong(editIdParam.trim());
                Reservation editTarget = reservationDao.getById(editId);
                if (editTarget != null) {
                    request.setAttribute("editReservation", editTarget);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid reservation ID for edit");
            }
        }

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

        // Resolve acting admin from session
        String userEmail = (String) session.getAttribute("userEmail");
        Administrator admin = adminDao.getAdministratorByEmail(userEmail);

        Long reservationId = Long.parseLong(request.getParameter("id"));
        String action = request.getParameter("action");

        Reservation reservation = reservationDao.getById(reservationId);

        if (reservation == null) {
            response.sendRedirect(request.getContextPath() + "/admin/reservations");
            return;
        }

        try {
            switch (action) {
                case "confirm": {
                    reservation.setStatus(ReservationStatus.CONFIRMED);
                    reservation.setHandledByAdminId(admin.getId());
                    reservationDao.updateWithRetry(reservation);

                    try {
                        actionDao.record(reservation, admin, ReservationActionType.CONFIRMED);
                    } catch (Exception auditEx) {
                        logger.warn("Confirmation failed for reservation {}: {}", reservationId, auditEx.getMessage());
                    }
                    break;
                }
                case "cancel": {
                    reservation.setStatus(ReservationStatus.CANCELLED);
                    reservation.setHandledByAdminId(admin.getId());
                    reservationDao.updateWithRetry(reservation);

                    try {
                        actionDao.record(reservation, admin, ReservationActionType.CANCELLED);
                    } catch (Exception auditEx) {
                        logger.warn("Cancellation failed for reservation {}: {}", reservationId, auditEx.getMessage());
                    }
                    break;
                }
                case "edit": {
                    reservation.setCustomerName(request.getParameter("customerName"));
                    reservation.setEmail(request.getParameter("email"));
                    reservation.setPartySize(Integer.parseInt(request.getParameter("partySize")));
                    reservation.setAllergenInfo(request.getParameter("allergenInfo"));
                    reservation.setAdditionalComments(request.getParameter("additionalComments"));
                    reservation.setHandledByAdminId(admin.getId());
                    reservationDao.updateWithRetry(reservation);

                    try {
                        actionDao.record(reservation, admin, ReservationActionType.UPDATED);
                    } catch (Exception auditEx) {
                        logger.warn("Audit record failed for reservation {}: {}", reservationId, auditEx.getMessage());
                    }

                    break;
                }
            }
        } catch (RuntimeException e) {
            request.getSession().setAttribute("error",
                    "Update failed - the reservation may have been modified by another admin. Please try again.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/reservations");
    }
}
