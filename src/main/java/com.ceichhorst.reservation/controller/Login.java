package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;

@WebServlet(
        urlPatterns = {"/login"}
)

/** Begins the authentication process using AWS Cognito
 *
 */
public class Login extends HttpServlet implements PropertiesLoader {
    Properties properties;
    private final Logger logger = LogManager.getLogger(this.getClass());
    public static String CLIENT_ID;
    public static String LOGIN_URL;
    public static String REDIRECT_URL;

    @Override
    public void init() throws ServletException {
        super.init();
        loadProperties();
    }

    /**
     * Read in the cognito props file and get the client id and required urls
     * for authenticating a user.
     */
    private void loadProperties() {
        properties = (Properties) getServletContext().getAttribute("cognitoProperties");
        if (properties == null) {
            logger.error("Cognito properties missing in ServletContext");
            return;
        }
        CLIENT_ID = properties.getProperty("client.id");
        LOGIN_URL = properties.getProperty("loginURL");
        REDIRECT_URL = properties.getProperty("redirectURL");
    }

    /**
     * Route to the aws-hosted cognito login page.
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userEmail") != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            return;
        }

        if (LOGIN_URL == null || LOGIN_URL.isEmpty() ||
                CLIENT_ID == null || CLIENT_ID.isEmpty() ||
                REDIRECT_URL == null || REDIRECT_URL.isEmpty()) {

            logger.error("Cognito properties not loaded properly. " +
                    "LOGIN_URL: " + LOGIN_URL +
                    ", CLINET_ID: " + CLIENT_ID +
                    ", REDIRECT_URL: " + REDIRECT_URL);

            resp.sendRedirect(req.getContextPath() + "/error.jsp");
            return;
        }
        String url = LOGIN_URL + "?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URL;
        resp.sendRedirect(url);
    }
}
