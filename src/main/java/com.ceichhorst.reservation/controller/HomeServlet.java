package com.ceichhorst.reservation.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

import com.ceichhorst.reservation.dao.*;
import com.ceichhorst.reservation.service.*;
import com.ceichhorst.reservation.entity.*;

import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

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
        } catch (Exception e) {
            request.setAttribute("message", "Error fetching data from database!");
            request.setAttribute("stackTrace", e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }
}