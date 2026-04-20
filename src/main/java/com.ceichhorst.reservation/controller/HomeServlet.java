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

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(HomeServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {

        List<ServiceInstance> services = null;
        List<Restaurant> restaurants = null;
        String stackTrace = null;

        try {
            ServiceInstanceDao dao = new ServiceInstanceDao();
            services = dao.getAll();
            request.setAttribute("services", services);

            RestaurantDao restaurantDao = new RestaurantDao();
            restaurants = restaurantDao.getAll();
            request.setAttribute("restaurants", restaurants);

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