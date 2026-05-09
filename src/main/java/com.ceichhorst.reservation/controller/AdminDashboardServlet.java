package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.service.ServiceReservationStats;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servlet responsible for rendering the administrative dashboard.
 *
 * <p>This servlet provides an overview of reservation activity and service
 * availability for administrators. It aggregates data across all restaurants
 * assigned to the logged-in administrator.</p>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Validate administrator session and authentication state</li>
 *   <li>Authorize access based on assigned restaurants</li>
 *   <li>Retrieve service instances across one or more restaurants</li>
 *   <li>Aggregate reservation metrics (counts and grouped statistics)</li>
 *   <li>Prepare dashboard data for view rendering</li>
 * </ul>
 *
 * <p><strong>Session requirements:</strong></p>
 * <ul>
 *   <li>{@code adminId} → identifies the logged-in administrator</li>
 *   <li>{@code userEmail} → used for display purposes</li>
 * </ul>
 *
 * <p><strong>Dashboard data includes:</strong></p>
 * <ul>
 *   <li>List of accessible restaurant IDs</li>
 *   <li>All {@link ServiceInstance} objects for those restaurants</li>
 *   <li>Total reservation count across all services</li>
 *   <li>Reservation statistics grouped by service date
 *       ({@link ServiceReservationStats})</li>
 * </ul>
 *
 * <p>If the administrator is not authenticated,
 * the user is redirected or shown an appropriate message.</p>
 *
 * @author ceichhorst
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    /**
     * DAO for retrieving {@link ServiceInstance} data.
     */
    private ServiceInstanceDao serviceDao = new ServiceInstanceDao();

    /**
     * DAO for retrieving {@link Reservation} data and statistics.
     */
    private ReservationDao reservationDao = new ReservationDao();

    /**
     * DAO for retrieving administrator data and access control information.
     */
    private AdministratorDao adminDao = new AdministratorDao();

    /**
     * Handles HTTP GET requests to display the admin dashboard.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates that a session exists and contains authentication data</li>
     *   <li>Retrieves the current administrator</li>
     *   <li>Determines which restaurants the administrator has access to</li>
     *   <li>Loads services and reservation metrics for those restaurants</li>
     *   <li>Populates request attributes for the dashboard view</li>
     *   <li>Forwards to the dashboard JSP</li>
     * </ol>
     *
     * <p><strong>Redirect conditions:</strong></p>
     * <ul>
     *   <li>No session or missing authentication → redirect to login</li>
     *   <li>Invalid administrator → redirect to login</li>
     * </ul>
     *
     * <p><strong>Request attributes set:</strong></p>
     * <ul>
     *   <li>{@code userEmail}</li>
     *   <li>{@code restaurants} → set of accessible restaurant IDs</li>
     *   <li>{@code services} → list of {@link ServiceInstance}</li>
     *   <li>{@code reservationCount} → total reservations across all services</li>
     *   <li>{@code reservationStats} → grouped statistics per service date</li>
     * </ul>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userEmail = (String) session.getAttribute("userEmail");
        request.setAttribute("userEmail", userEmail);
        request.setAttribute("username", session.getAttribute("username"));

        // Admin user check
        Long adminId = (Long) session.getAttribute("adminId");

        if (adminId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Administrator admin = adminDao.getById(adminId);

        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Check if restaurants are available to admin
        Set<Long> restaurantIds = adminDao.getRestaurantIds(adminId);

        if (restaurantIds.isEmpty()) {
            request.setAttribute("message", "No restaurants assigned.");
            request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp")
                    .forward(request, response);
            return;
        }

        List<ServiceInstance> services = serviceDao.getServicesByRestaurants(restaurantIds);
        Long reservationCount = reservationDao.countReservationsByService(restaurantIds);
        List<ServiceReservationStats> reservationStats = reservationDao.countReservationsGroupedByService(restaurantIds);

        request.setAttribute("restaurants", restaurantIds);
        request.setAttribute("services", services);
        request.setAttribute("reservationCount", reservationCount);
        request.setAttribute("reservationStats", reservationStats);

        request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp")
                .forward(request, response);
    }
}
