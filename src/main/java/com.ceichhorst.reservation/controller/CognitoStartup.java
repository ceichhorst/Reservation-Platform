package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.util.PropertiesLoader;

import java.io.IOException;
import java.util.Properties;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

/**
 * Startup to load properties from cognito.proeprties in order to login via Cognito
 */
@WebServlet(name = "cognitoStartup", urlPatterns = {"/cognito-startup"}, loadOnStartup = 1)
public class CognitoStartup extends HttpServlet implements PropertiesLoader {
    @Override
    public void init() throws ServletException {
        try {
            Properties props = loadProperties("/cognito.properties");
            getServletContext().setAttribute("cognitoProperties", props);
            System.out.println("Cognito properties loaded successfully.");
        } catch (IOException e) {
            throw new ServletException("Failed to load cognito properties.");
        } catch (Exception e) {
            throw new ServletException("Unexpected error in CognitoStartup.", e);
        }
    }
}
