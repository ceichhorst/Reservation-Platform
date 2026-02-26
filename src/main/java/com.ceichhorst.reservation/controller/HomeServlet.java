package com.ceichhorst.reservation.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {

        request.setAttribute("message", "Servlet is working!");
        request.getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }
}