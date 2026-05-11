package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.util.PropertiesLoader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;
import java.nio.charset.StandardCharsets;

/**
 * Servlet that directs the Cognito logout to the logout success page
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet implements PropertiesLoader {

    private static final Logger logger = LogManager.getLogger(LogoutServlet.class);

    private Properties properties;
    private String CLIENT_ID;
    private String LOGOUT_URL;

    @Override
    public void init() throws ServletException {
        try {
            properties = loadProperties("/cognito.properties");
            CLIENT_ID = properties.getProperty("client.id");
            LOGOUT_URL = properties.getProperty("logoutURL");
            logger.info("Cognito Properties loaded successfully during logout.");
        } catch (Exception e) {
            logger.error("Unable to load Cognito Properties", e);
            throw new ServletException("Unable to load Cognito Properties", e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        String postLogoutPath;

        if (session != null) {

            Long restaurantId = (Long) session.getAttribute("lastRestaurantId");

            if (restaurantId != null) {
                postLogoutPath = "/r/" + restaurantId;
            } else {
                postLogoutPath = "/admin/dashboard";
            }

            session.invalidate();

        } else {
            postLogoutPath = "/admin/dashboard";
        }

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("postLogoutPath", postLogoutPath);

        String redirectUri = request.getContextPath() + "/logout-success";

        String redirectUrl = LOGOUT_URL
                + "?client_id=" + CLIENT_ID
                + "&logout_uri=" + URLEncoder.encode(
                request.getScheme() + "://"
                        + request.getServerName() + ":"
                        + request.getServerPort()
                        + redirectUri,
                StandardCharsets.UTF_8
        );

        response.sendRedirect(redirectUrl);
    }
}
