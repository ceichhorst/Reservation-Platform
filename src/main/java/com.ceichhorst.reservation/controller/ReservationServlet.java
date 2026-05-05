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
     * DAO used to retrieve {@link ServiceInstance} data.
     */
    private ServiceInstanceDao serviceInstanceDao;

    /**
     * Utility for formatting service times for display.
     */
    private final ServiceTimeFormatter formatter = new ServiceTimeFormatter();

    /**
     * Initializes the servlet and its dependencies.
     */
    @Override
    public void init() {
        serviceInstanceDao = new ServiceInstanceDao();

    }

    /**
     * Handles HTTP GET requests for displaying reservation availability.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates and parses the restaurant ID from request parameters</li>
     *   <li>Retrieves the {@link Restaurant} and associated {@link ServiceInstance}s</li>
     *   <li>Builds a calendar view of availability using {@link AvailabilityService}</li>
     *   <li>If a date is selected, filters available time slots for that date</li>
     *   <li>Formats service times for display</li>
     *   <li>Stores data in request and session attributes for rendering</li>
     * </ol>
     *
     * <p><strong>Request parameters:</strong></p>
     * <ul>
     *   <li>{@code restaurantId} (required): the target restaurant</li>
     *   <li>{@code date} (optional): ISO-8601 date string (e.g., {@code yyyy-MM-dd})</li>
     * </ul>
     *
     * <p><strong>Request attributes set:</strong></p>
     * <ul>
     *   <li>{@code restaurant}</li>
     *   <li>{@code services}</li>
     *   <li>{@code calendar}</li>
     *   <li>{@code availableTimes} (if date selected)</li>
     *   <li>{@code selectedDate} (if provided)</li>
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

        HttpSession session = request.getSession();
        Restaurant sessionRestaurant = (Restaurant) session.getAttribute("restaurant");

        String dateParam = request.getParameter("date");
        String restaurantIdStr = request.getParameter("restaurantId");

        if (restaurantIdStr == null || restaurantIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing restaurant id");
            return;
        }

        Long restaurantId = Long.parseLong(restaurantIdStr);

        try {
            ServiceInstanceDao serviceDao = new ServiceInstanceDao();
            RestaurantDao restaurantDao = new RestaurantDao();
            AvailabilityService availabilityService = new AvailabilityService();

            Restaurant restaurant = restaurantDao.getById(restaurantId);
            List<ServiceInstance> services = serviceDao.getByRestaurantId(restaurantId);
            List<DayAvailability> calendar = availabilityService.buildCalendar(services);

            if (sessionRestaurant == null || !sessionRestaurant.getId().equals(restaurantId)) {
                session.setAttribute("restaurant", restaurant);
            }

            request.setAttribute("restaurant", restaurant);
            request.setAttribute("services", services);
            request.setAttribute("calendar", calendar);

            if (dateParam != null && !dateParam.isEmpty()) {

                request.setAttribute("selectedDate", dateParam);

                try {
                    LocalDate date = LocalDate.parse(dateParam);

                    List<ServiceInstance> availableTimes =
                            availabilityService.getAvailableTimes(
                                    restaurant,
                                    services,
                                    date
                            );

                    availableTimes = formatter.formatTimes(availableTimes);
                    request.setAttribute("availableTimes", availableTimes);

                } catch (Exception e) {
                    request.setAttribute("message", "Invalid date format.");
                }
            }

        } catch (Exception e) {
            request.setAttribute("message", "Error loading page data.");
            e.printStackTrace();
        }

        request.getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }

    /**
     * Handles HTTP POST requests for selecting a specific reservation time.
     *
     * <p>This method processes the user's selection of a {@link ServiceInstance}
     * and prepares data for the reservation details page.</p>
     *
     * <p>It performs the following steps:</p>
     * <ol>
     *   <li>Validates required input parameters</li>
     *   <li>Retrieves the selected {@link ServiceInstance}</li>
     *   <li>Prepares reservation summary data (date, time, party size)</li>
     *   <li>Forwards the request to the reservation details page</li>
     * </ol>
     *
     * <p><strong>Request parameters:</strong></p>
     * <ul>
     *   <li>{@code restaurantId} (required)</li>
     *   <li>{@code serviceInstanceId} (required)</li>
     *   <li>{@code partySize} (required)</li>
     * </ul>
     *
     * <p><strong>Request attributes set:</strong></p>
     * <ul>
     *   <li>{@code restaurantId}</li>
     *   <li>{@code reservationDate}</li>
     *   <li>{@code reservationTime}</li>
     *   <li>{@code partySize}</li>
     * </ul>
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if processing fails
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String restaurantIdStr = request.getParameter("restaurantId");
        String serviceInstanceIdStr = request.getParameter("serviceInstanceId");
        String partySizeStr = request.getParameter("partySize");

        if (restaurantIdStr == null || restaurantIdStr.isEmpty()
                || serviceInstanceIdStr == null || serviceInstanceIdStr.isEmpty()
                || partySizeStr == null || partySizeStr.isEmpty()) {

            request.setAttribute("message", "Missing required fields");
            request.getRequestDispatcher("/WEB-INF/index.jsp")
                    .forward(request, response);
            return;
        }

        try {
            Long restaurantId = Long.parseLong(restaurantIdStr);
            Long serviceInstanceId = Long.parseLong(serviceInstanceIdStr);
            int partySize = Integer.parseInt(partySizeStr);

            ServiceInstance instance = serviceInstanceDao.getById(serviceInstanceId);

            if (instance == null) {
                request.setAttribute("message", "Invalid service selection");
                request.getRequestDispatcher("/WEB-INF/index.jsp")
                        .forward(request, response);
                return;
            }

            request.setAttribute("restaurantId", restaurantId);
            request.setAttribute("reservationDate", instance.getServiceDate().toString());
            request.setAttribute("reservationTime", instance.getServiceTimeFormatted());
            request.setAttribute("partySize", partySize);

            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error processing reservation", e);
        }
    }
}
