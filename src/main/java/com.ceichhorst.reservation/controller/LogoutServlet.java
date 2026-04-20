package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.util.PropertiesLoader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;
import java.nio.charset.StandardCharsets;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet implements PropertiesLoader {

    private Properties properties;
    private String CLIENT_ID;
    private String LOGOUT_URL;
    private String LOGOUT_REDIRECT;

    @Override
    public void init() throws ServletException {
        try {
            properties = loadProperties("/cognito.properties");
            CLIENT_ID = properties.getProperty("client.id");
            LOGOUT_URL = properties.getProperty("logoutURL");
            LOGOUT_REDIRECT = properties.getProperty("logoutRedirect");
        } catch (Exception e) {
            throw new ServletException("Unable to load Cognito Properties", e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        String redirectUrl = LOGOUT_URL
                + "?client_id=" + CLIENT_ID
                + "&logout_uri=" + LOGOUT_REDIRECT;

        response.sendRedirect(redirectUrl);
    }
}
