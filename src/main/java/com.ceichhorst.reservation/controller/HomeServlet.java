package com.ceichhorst.reservation.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServlet request, HttpServletResponse responsed)
             throws ServletException, IOException {

        request.setAttribute("message", "Servlet is working!");
        request.getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }
}