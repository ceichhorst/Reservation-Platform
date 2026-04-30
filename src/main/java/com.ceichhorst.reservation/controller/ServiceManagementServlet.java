package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceInstance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@WebServlet("/admin/services")
public class ServiceManagementServlet extends HttpServlet {
    private ServiceInstanceDao serviceDao = new ServiceInstanceDao();
    private AdministratorDao adminDao = new AdministratorDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long adminId = (Long) session.getAttribute("adminId");

        if (adminId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get restaurants assigned to admin
        List<Restaurant> restaurants = adminDao.getRestaurantByAdminId(adminId);
        request.setAttribute("restaurants", restaurants);

        // Selected restaurant
        String restaurantIdParam = request.getParameter("restaurantId");

        if (restaurantIdParam != null) {
            Long restaurantId = Long.parseLong(restaurantIdParam);

            List<ServiceInstance> services = serviceDao.getByRestaurantId(restaurantId);

            request.setAttribute("services", services);
            request.setAttribute("selectedRestaurantId", restaurantId);

            Restaurant selected = restaurants.stream()
                    .filter(r -> r.getId().equals(restaurantId))
                    .findFirst()
                    .orElse(null);

            if (selected != null) {
                request.setAttribute("scheduleType", selected.getSchedulingType());
            }
        }

        request.getRequestDispatcher("/WEB-INF/admin/services.jsp")
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

    }
}
