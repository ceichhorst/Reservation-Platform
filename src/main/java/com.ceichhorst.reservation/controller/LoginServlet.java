package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.entity.Administrator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        AdministratorDao dao = new AdministratorDao();
        Administrator admin = dao.getAdministratorByUsername(username);

        if (admin != null && password.equals(admin.getPasswordHash())) {
            HttpSession session = request.getSession();
            session.setAttribute("adminUser", admin);

            response.sendRedirect("admin/dashboard");
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/login.jsp")
                    .forward(request, response);
        }
    }
}
