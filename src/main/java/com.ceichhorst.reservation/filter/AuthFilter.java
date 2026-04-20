package com.ceichhorst.reservation.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/admin/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        String userEmail = (session != null) ? (String) session.getAttribute("userEmail") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // Not logged in
        if (userEmail == null) {
            String loginUrl = (String) req.getServletContext().getAttribute("LOGIN_URL");

            if (loginUrl == null) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login URL not configured");
                return;
            }

            res.sendRedirect(loginUrl);
            return;
        }

        if (!"ADMIN".equals(role)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}
