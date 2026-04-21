package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;
import com.ceichhorst.reservation.service.ServiceInstance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

// Core user flow component to confirm a reservation made by a customer for a restaurant
@WebServlet("/r/*/confirm-reservation")
public class ConfirmationServlet extends HttpServlet{

    private ReservationDao reservationDao = new ReservationDao();
    private ServiceInstanceDao serviceInstanceDao = new ServiceInstanceDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("customerName");
        String email = request.getParameter("email");
        String dateStr = request.getParameter("reservationDate");
        String timeStr = request.getParameter("reservationTime");
        String partySizeStr = request.getParameter("partySize");
        String restaurantIdStr = request.getParameter("restaurantId");
        String allergies = request.getParameter("guestAllergens");
        String note = request.getParameter("guestNotes");

        // Convert types
        Long restaurantId = Long.parseLong(restaurantIdStr);
        LocalDate date = LocalDate.parse(dateStr);
        int partySize = Integer.parseInt(partySizeStr);

        // Find matching service instance
        List<ServiceInstance> services = serviceInstanceDao.getServicesByRestaurantOnDate(restaurantId, date);

        LocalTime time = LocalTime.parse(timeStr);
        ServiceInstance selected = services.stream()
                .filter(s -> s.getServiceTime().equals(time))
                .findFirst()
                .orElse(null);

        if (selected == null) {
            request.setAttribute("message", "Invalid time selection.");
            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);
            return;
        }

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setCustomerName(name);
        reservation.setEmail(email);
        reservation.setServiceInstance(selected);
        reservation.setPartySize(partySize);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setAllergenInfo(allergies);
        reservation.setAdditionalComments(note);

        // TODO: capacity check here
        boolean success = reservationDao.createReservationIfAvailable(reservation);
        if (!success) {
            request.setAttribute("message", "That time slot is full.");
            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);
            return;
        }

        // Send to confirmation page
        request.setAttribute("customerName", name);
        request.setAttribute("reservationDate", dateStr);
        request.setAttribute("reservationTime", timeStr);
        request.setAttribute("partySize", partySize);
        request.setAttribute("confirmationId", reservation.getId());
        request.setAttribute("guestAllergens", allergies);
        request.setAttribute("guestComments", note);

        request.getRequestDispatcher("/WEB-INF/confirm-reservation.jsp")
                .forward(request, response);

    }
}
