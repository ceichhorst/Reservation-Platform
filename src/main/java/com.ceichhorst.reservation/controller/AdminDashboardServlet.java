package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.dao.ReservationDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private ServiceInstanceDao serviceDao = new ServiceInstanceDao();
    private ReservationDao reservationDao = new ReservationDao();
    private AdministratorDao adminDao = new AdministratorDao();

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

        request.setAttribute("restaurants", restaurantIds);
        request.setAttribute("services", services);
        request.setAttribute("reservationCount", reservationCount);

        request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp")
                .forward(request, response);
    }
}
