package com.ceichhorst.reservation.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

import com.ceichhorst.reservation.dao.*;
import com.ceichhorst.reservation.service.*;
import com.ceichhorst.reservation.entity.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@WebServlet("/r/*")
public class HomeServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(HomeServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing restaurant ID");
            return;
        }

        String[] parts = pathInfo.split("/");

        Long restaurantId;
        try {
            restaurantId = Long.parseLong(parts[1]);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Restaruant ID");
            return;
        }

        String page = (parts.length > 2) ? parts[2] : "home";

        List<ServiceInstance> services = null;
        Restaurant restaurant = null;

        try {
            ServiceInstanceDao serviceDao = new ServiceInstanceDao();
            RestaurantDao restaurantDao = new RestaurantDao();
            AvailabilityService availabilityService = new AvailabilityService();

            restaurant = restaurantDao.getById(restaurantId);
            services = serviceDao.getByRestaurantId(restaurantId);
            List<DayAvailability> calendar = availabilityService.buildCalendar(services);

            request.setAttribute("restaurant", restaurant);

            request.setAttribute("services", services);
            request.setAttribute("calendar", calendar);

            String message = "Servlet is working!";
            logger.info("Successfully retrieved services and restaurants");
        } catch (Exception e) {
            logger.error("Error fetching data from database", e);
            request.setAttribute("message", "Error fetching data from database!");
        }

        request.getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }
}