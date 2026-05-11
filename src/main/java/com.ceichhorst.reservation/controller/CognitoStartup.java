package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.util.PropertiesLoader;

import java.io.IOException;
import java.util.Properties;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Startup to load properties from cognito.proeprties in order to login via Cognito
 */
@WebServlet(name = "cognitoStartup", urlPatterns = {"/cognito-startup"}, loadOnStartup = 1)
public class CognitoStartup extends HttpServlet implements PropertiesLoader {

    private static final Logger logger = LogManager.getLogger(CognitoStartup.class);

    @Override
    public void init() throws ServletException {
        try {
            Properties props = loadProperties("/cognito.properties");
            getServletContext().setAttribute("cognitoProperties", props);
            logger.info("Cognito properties loaded successfully.");
        } catch (IOException e) {
            logger.error("Cognito properties failed to load", e);
            throw new ServletException("Failed to load Cognito properties.");
        } catch (Exception e) {
            logger.error("Unexpected error in CognitoStartup", e);
            throw new ServletException("Unexpected error in CognitoStartup.", e);
        }
    }
}
