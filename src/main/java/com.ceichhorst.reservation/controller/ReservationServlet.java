package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;

import org.hibernate.Session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;

@WebServlet("/r/*/reservation")
public class ReservationServlet extends HttpServlet {
    // Adding variable for ReservationDao for validating availability down the road
    private ReservationDao reservationDao;

    @Override
    public void init() {
        reservationDao = new ReservationDao();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dateParam = request.getParameter("date");

        if (dateParam != null && !dateParam.isEmpty()) {

            try {
                LocalDate date = LocalDate.parse(dateParam);

                ServiceInstanceDao dao = new ServiceInstanceDao();

                List<ServiceInstance> instances = dao.getByDate(date);

                request.setAttribute("availableTimes", instances);

            } catch (Exception e) {
                request.setAttribute("message", "Invalid date format.");
            }
        }

        request.getRequestDispatcher("/WEB-INF/index.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing restaurant id");
            return;
        }

        String[] parts = pathInfo.split("/");

        Long restaurantId;
        try {
            restaurantId = Long.parseLong(parts[1]);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid restaurant id");
            return;
        }

        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String partySizeStr = request.getParameter("partySize");

        if (date == null || time == null || partySizeStr == null ||
            date.isEmpty() || time.isEmpty() || partySizeStr.isEmpty()) {

            request.setAttribute("message", "All fields are required.");
            request.getRequestDispatcher("/WEB-INF/index.jsp")
                    .forward(request, response);
            return;
        }

        int partySize = Integer.parseInt(partySizeStr);


        request.setAttribute("restaurantId", restaurantId);
        request.setAttribute("reservationDate", date);
        request.setAttribute("reservationTime", time);
        request.setAttribute("partySize", partySize);

        request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                .forward(request, response);

    }
}
