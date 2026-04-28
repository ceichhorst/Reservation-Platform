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
import com.ceichhorst.reservation.util.HibernateUtil;

import org.hibernate.Session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;

// Core user flow component for customers to make reservations
@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {

    private ServiceInstanceDao serviceInstanceDao;

    @Override
    public void init() {
        serviceInstanceDao = new ServiceInstanceDao();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

            request.setAttribute("restaurant", restaurant);
            request.setAttribute("services", services);
            request.setAttribute("calendar", calendar);

            if (dateParam != null && !dateParam.isEmpty()) {

                request.setAttribute("selectedDate", dateParam);

                try {
                    LocalDate date = LocalDate.parse(dateParam);

                    List<ServiceInstance> instances =
                            serviceDao.getServicesByRestaurantOnDate(restaurantId, date);

                    request.setAttribute("availableTimes", instances);

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
            request.setAttribute("reservationTime", instance.getServiceTime().toString());
            request.setAttribute("partySize", partySize);

            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error processing reservation", e);
        }
    }
}
