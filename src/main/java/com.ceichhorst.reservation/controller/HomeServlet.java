package com.ceichhorst.reservation.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.StringWriter;

import com.ceichhorst.reservation.dao.*;
import com.ceichhorst.reservation.service.*;

import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {

        List<ServiceInstance> services = null;
        String message = "Servlet is working!";
        String stackTrace = null;

        try {
            ServiceInstanceDao dao = new ServiceInstanceDao();
            services = dao.getAll();
        } catch (Exception e) {
            message = "Error fetching services: " + e.getMessage();
            StringWriter sw = new StringWriter();
            e.printStackTrace();
            stackTrace = sw.toString();
        }

        request.setAttribute("message", message);
        request.setAttribute("services", services);
        request.setAttribute("stackTrace", stackTrace);
        request.getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }
}