package com.ceichhorst.reservation.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

import com.ceichhorst.reservation.dao.*;
import com.ceichhorst.reservation.service.*;

import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {

        ServiceInstanceDao dao = new ServiceInstanceDao();
        List<ServiceInstance> services = dao.getAll();

        request.setAttribute("message", "Servlet is working!");
        request.getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }
}