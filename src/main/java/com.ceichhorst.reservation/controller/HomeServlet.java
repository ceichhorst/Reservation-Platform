package com.ceichhorst.reservation.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

import com.ceichhorst.reservation.dao.*;
import com.ceichhorst.reservation.service.*;
import com.ceichhorst.reservation.entity.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Servlet responsible for handling requests to the public-facing home page
 * of the reservation platform.
 *
 * <p>This servlet serves a restaurant-specific home page using a path-based routing
 * scheme. Each request is scoped to a particular {@link Restaurant}, identified
 * by an ID embedded in the URL.</p>
 *
 * <p><strong>URL format:</strong></p>
 * <pre>
 *   /r/{restaurantId}
 * </pre>
 *
 * <p>Example:</p>
 * <ul>
 *   <li>{@code /r/1} → Loads the home page for restaurant with ID 1</li>
 * </ul>
 *
 * @author ceichhorst
 */
@WebServlet("/r/*")
public class HomeServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(HomeServlet.class);

    /**
     * Utility for formatting service times for display.
     */
    private final ServiceTimeFormatter formatter = new ServiceTimeFormatter();

    /**
     * Handles HTTP GET requests for restaurant-specific home page.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Parses the restaurant ID from the request path</li>
     *   <li>Retrieves the associated {@link Restaurant} and its {@link ServiceInstance}s</li>
     *   <li>Filters out non-visible services</li>
     *   <li>If a date parameter is provided, computes available time slots</li>
     *   <li>Formats service times for display</li>
     *   <li>Builds a calendar view of availability</li>
     *   <li>Stores relevant data in request and session attributes</li>
     *   <li>Forwards the request to the JSP view for rendering</li>
     * </ol>
     *
     * <p><strong>Request attributes set:</strong></p>
     * <ul>
     *   <li>{@code restaurant} → the current {@link Restaurant}</li>
     *   <li>{@code services} → all visible {@link ServiceInstance}s</li>
     *   <li>{@code availableTimes} → filtered available time slots for selected date</li>
     *   <li>{@code calendar} → list of {@link DayAvailability} objects</li>
     *   <li>{@code selectedDate} → the selected date (if provided)</li>
     * </ul>
     *
     * <p><strong>Session attributes set:</strong></p>
     * <ul>
     *   <li>{@code restaurant} → cached restaurant object</li>
     *   <li>{@code lastRestaurantId} → last accessed restaurant ID</li>
     * </ul>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if request forwarding fails
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing restaurant ID");
            return;
        }

        String[] parts = pathInfo.split("/");

        Long restaurantId;
        try {
            restaurantId = Long.parseLong(parts[1]);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Restaurant ID");
            return;
        }
        request.getSession().setAttribute("lastRestaurantId", restaurantId);

        // TODO is this needed anymore?
        String page = (parts.length > 2) ? parts[2] : "home";

        List<ServiceInstance> services = null;
        Restaurant restaurant = null;

        try {
            ServiceInstanceDao serviceDao = new ServiceInstanceDao();
            RestaurantDao restaurantDao = new RestaurantDao();
            AvailabilityService availabilityService = new AvailabilityService();

            restaurant = restaurantDao.getById(restaurantId);
            services = serviceDao.getByRestaurantId(restaurantId);

            // Check what service dates are active or hidden
            services = services.stream()
                    .filter(ServiceInstance::getVisible)
                    .collect(Collectors.toList());

            String selectedDateParam = request.getParameter("date");
            List<ServiceInstance> availableTimes = new ArrayList<>();

            if (selectedDateParam != null && !selectedDateParam.isEmpty()) {
                LocalDate selectedDate = LocalDate.parse(selectedDateParam);

                availableTimes = availabilityService.getAvailableTimes(
                        restaurant,
                        services,
                        selectedDate
                );

                request.setAttribute("selectedDate", selectedDate);
            }

            services = formatter.formatTimes(services);
            availableTimes = formatter.formatTimes(availableTimes);

            request.setAttribute("availableTimes", availableTimes);

            // Build availability calendar
            List<DayAvailability> calendar = availabilityService.buildCalendar(services);

            request.setAttribute("restaurant", restaurant);
            request.getSession().setAttribute("restaurant", restaurant);

            request.setAttribute("services", services);
            request.setAttribute("calendar", calendar);

            String message = "Servlet is working!";
            logger.info("Successfully retrieved services and restaurants");
        } catch (Exception e) {
            logger.error("Error fetching data from database", e);
            request.setAttribute("message", "Error fetching data from database!");
        }

        request.getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }
}