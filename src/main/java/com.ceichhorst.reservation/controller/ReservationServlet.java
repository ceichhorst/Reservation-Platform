package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.dao.RestaurantDao;
import com.ceichhorst.reservation.service.AvailabilityService;
import com.ceichhorst.reservation.service.DayAvailability;
import com.ceichhorst.reservation.service.ReservationService;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.service.ServiceTimeFormatter;
import com.ceichhorst.reservation.util.HibernateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;

/**
 * Servlet responsible for handling the customer reservation workflow.
 *
 * <p>This servlet supports both displaying available reservation options and
 * progressing the user through the reservation process.</p>
 *
 * <p><strong>URL mapping:</strong> {@code /reservation}</p>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Load restaurant and service data</li>
 *   <li>Display availability calendar and time slots</li>
 *   <li>Handle user selection of a specific service instance</li>
 *   <li>Forward users to the reservation details step</li>
 * </ul>
 *
 * <p>This servlet works in conjunction with {@link AvailabilityService} for
 * availability calculations and {@link ServiceTimeFormatter} for preparing
 * display-friendly time values.</p>
 *
 * @author ceichhorst
 */
@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {

    /**
     * DAO used to retrieve {@link Restaurant} data
     */
    private RestaurantDao restaurantDao;

    /**
     * DAO used to retrieve {@link ServiceInstance} data.
     */
    private ServiceInstanceDao serviceInstanceDao;

    /**
     * Utility for formatting service times for display.
     */
    private final ServiceTimeFormatter formatter = new ServiceTimeFormatter();

    private static final Logger logger = LogManager.getLogger(ReservationServlet.class);

    /**
     * Initializes the servlet and its dependencies.
     */
    @Override
    public void init() {
        restaurantDao = new RestaurantDao();
        serviceInstanceDao = new ServiceInstanceDao();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String restaurantIdStr = request.getParameter("restaurantId");
        String serviceInstanceIdStr = request.getParameter("serviceInstanceId");
        String partySizeStr = request.getParameter("partySize");

        if (restaurantIdStr == null || restaurantIdStr.isEmpty()
                || serviceInstanceIdStr == null || serviceInstanceIdStr.isEmpty()
                || partySizeStr == null || partySizeStr.isEmpty()) {
            logger.error("Missing required fiels");
            request.setAttribute("message", "Missing required fields");
            request.getRequestDispatcher("/WEB-INF/index.jsp")
                    .forward(request, response);
            return;
        }

        try {
            Long restaurantId = Long.parseLong(restaurantIdStr);
            Long serviceInstanceId = Long.parseLong(serviceInstanceIdStr);
            int partySize = Integer.parseInt(partySizeStr);

            Restaurant restaurant = restaurantDao.getById(restaurantId);

            if (restaurant == null) {
                logger.error("Invalid restaurant");
                request.setAttribute("message", "Invalid restaurant");
                request.getRequestDispatcher("/WEB-INF/index.jsp")
                        .forward(request, response);
                return;
            }

            ServiceInstance instance = serviceInstanceDao.getById(serviceInstanceId);

            List<ServiceInstance> formatted = formatter.formatTimes(List.of(instance));

            instance = formatted.get(0);

            if (instance == null) {
                logger.error("Invalid service selection");
                request.setAttribute("message", "Invalid service selection");
                request.getRequestDispatcher("/WEB-INF/index.jsp")
                        .forward(request, response);
                return;
            }

            request.setAttribute("restaurantId", restaurantId);
            request.setAttribute("serviceInstanceId", serviceInstanceId);
            request.setAttribute("reservationDate", instance.getServiceDate().toString());
            request.setAttribute("reservationTime", instance.getServiceTimeFormatted());
            request.setAttribute("partySize", partySize);
            request.setAttribute("requireAllergenInfo", restaurant.isRequireAllergenInfo());

            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            logger.error("Error processing reservation", e);
            throw new ServletException("Error processing reservation", e);
        }
    }
}
