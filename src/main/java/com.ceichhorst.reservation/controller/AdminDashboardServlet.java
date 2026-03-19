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
        Administrator admin = (Administrator) session.getAttribute("adminUser");

        if (admin == null) {
            response.sendRedirect("../login");
            return;
        }

        Set<Restaurant> restaurants = admin.getRestaurants();

        request.setAttribute("restaurants", restaurants);

        request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp")
                .forward(request, response);

        response.sendRedirect("/login");
    }
}
