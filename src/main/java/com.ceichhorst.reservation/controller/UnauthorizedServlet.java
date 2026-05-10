package com.ceichhorst.reservation.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that handles unauthorized access attempts
 *
 * @author ceichhorst
 */
@WebServlet("/unahtorized")
public class UnauthorizedServlet extends HttpServlet {

    @Override
    public void doget(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        request.getRequestDispatcher("/WEB-INF/error/unauthorized.jsp")
                .forward(request,response);
    }
}
