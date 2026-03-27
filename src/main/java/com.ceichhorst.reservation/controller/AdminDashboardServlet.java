package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Set;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

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
/*
        Administrator admin = (Administrator) session.getAttribute("adminUser");

        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        }

        Set<Restaurant> restaurants = admin.getRestaurants();

        request.setAttribute("restaurants", restaurants); */

        request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp")
                .forward(request, response);
    }
}
