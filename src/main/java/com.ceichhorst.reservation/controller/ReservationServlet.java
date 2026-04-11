package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.util.HibernateUtil;

import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {
    // Adding variable for ReservationDao for validating availability down the road
    private ReservationDao reservationDao;

    @Override
    public void init() {
        reservationDao = new ReservationDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        request.setAttribute("date", date);
        request.setAttribute("time", time);
        request.setAttribute("partySize", partySize);

        request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                .forward(request, response);

    }
}
