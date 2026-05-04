package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.dao.RestaurantDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.service.ServiceManager;
import com.ceichhorst.reservation.service.ServiceTimeFormatter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/admin/services")
public class ServiceManagementServlet extends HttpServlet {
    private ServiceInstanceDao serviceDao = new ServiceInstanceDao();
    private AdministratorDao adminDao = new AdministratorDao();
    private ServiceManager serviceManager = new ServiceManager();
    private final ServiceTimeFormatter formatter = new ServiceTimeFormatter();

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

        if (restaurantIdParam != null && !restaurantIdParam.isEmpty()) {
            final Long restaurantId = Long.parseLong(restaurantIdParam);

            // Filtering variables for Existing Services table
            String filterType = request.getParameter("filterType");
            String filterDate = request.getParameter("filterDate");
            String filterMonth = request.getParameter("filterMonth");

            List<ServiceInstance> services = serviceDao.getByRestaurantId(restaurantId);

            // Filtering
            if (filterType != null) {
                switch (filterType) {
                    case "DATE":
                        if (filterDate != null && !filterDate.isEmpty()) {
                            LocalDate date = LocalDate.parse(filterDate);
                            services.stream()
                                    .filter(s -> s.getServiceDate().equals(date))
                                    .collect(Collectors.toList());
                        }
                        break;

                    case "MONTH":
                        if (filterDate != null && !filterDate.isEmpty()) {
                            String[] parts = filterMonth.split("-");
                            int year = Integer.parseInt(parts[0]);
                            int month = Integer.parseInt(parts[1]);
                            services.stream()
                                    .filter(s ->
                                            s.getServiceDate().getYear() == year &&
                                            s.getServiceDate().getMonthValue() == month
                                    )
                                    .collect(Collectors.toList());
                        }
                        break;

                    case "ALL":
                    default:
                        break;
                }
            }

            services = formatter.formatTimes(services);

            request.setAttribute("services", services);
            request.setAttribute("selectedRestaurantId", restaurantId);

            Restaurant selected = new RestaurantDao().getById(restaurantId);

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

        Long adminId = (Long) session.getAttribute("adminId");
        String action = request.getParameter("action");
        Long restaurantId = null;

        try {
            switch (action) {
                case "addService": {
                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));
                    LocalDate date = LocalDate.parse(request.getParameter("date"));
                    LocalTime time = LocalTime.parse(request.getParameter("time"));
                    LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
                    int capacity = Integer.parseInt(request.getParameter("capacity"));

                    serviceManager.addService(adminId, restaurantId, date, time, endTime, capacity);
                    break;
                }

                case "bulkDelete": {
                    String[] serviceIds = request.getParameterValues("serviceIds");

                    if (serviceIds != null) {
                        for (String id : serviceIds) {
                            serviceManager.deleteService(adminId, Long.parseLong(id));
                        }
                    }

                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));
                    break;
                }

                case "updateService": {
                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));
                    String schedulingType = request.getParameter("scheduleType");
                    serviceManager.updateSchedulingType(adminId, restaurantId, schedulingType);
                    break;
                }

                case "bulkToggleVisibility": {
                    String[] serviceIds = request.getParameterValues("serviceIds");

                    if (serviceIds != null) {
                        for (String id : serviceIds) {
                            serviceManager.toggleVisibility(adminId, Long.parseLong(id));
                        }
                    }

                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));
                    break;
                }
            }

        } catch (RuntimeException e) {
            request.getSession().setAttribute("error", e.getMessage());
        }

        if (restaurantId != null) {
            response.sendRedirect(
                    request.getContextPath() + "/admin/services?restaurantId=" + restaurantId
            );
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/services");
        }

    }
}
