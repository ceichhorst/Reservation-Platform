package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.service.ReservationService;
import com.ceichhorst.reservation.service.ReservationResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Core user flow component to confirm a reservation made by a customer for a restaurant
@WebServlet("/confirm-reservation")
public class ConfirmationServlet extends HttpServlet{

    private ReservationService reservationService = new ReservationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Extract form data
        String name = request.getParameter("customerName");
        String email = request.getParameter("email");
        String dateStr = request.getParameter("reservationDate");
        String timeStr = request.getParameter("reservationTime");
        String partySizeStr = request.getParameter("partySize");
        String restaurantIdStr = request.getParameter("restaurantId");
        String allergies = request.getParameter("guestAllergies");
        String note = request.getParameter("guestNotes");

        // Validation
        if (name == null || email == null || dateStr == null || timeStr == null ||
            partySizeStr == null || restaurantIdStr == null || name.isEmpty() ||
            email.isEmpty() || dateStr.isEmpty() || timeStr.isEmpty() || partySizeStr.isEmpty() ||
            restaurantIdStr.isEmpty()) {

            request.setAttribute("message", "All required fields must be filled out.");
            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);
            return;
        }

        // Convert types
        Long restaurantId;
        int partySize;

        try {
            partySize = Integer.parseInt(partySizeStr);
            restaurantId = Long.parseLong(restaurantIdStr);
        } catch (Exception e) {
            request.setAttribute("message", "Invalid input values.");
            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);
            return;
        }

        // Find matching service instance
        ReservationResult result = reservationService.createReservation(
                restaurantId,
                dateStr,
                timeStr,
                partySize,
                name,
                email,
                allergies,
                note
        );

        // Handle failure
        if (!result.isSuccess()) {
            request.setAttribute("message", result.getMessage());
            // Preserve previously entered details
            request.setAttribute("reservationDate", dateStr);
            request.setAttribute("reservationTime", timeStr);
            request.setAttribute("partySize", partySize);
            request.setAttribute("restaurantId", restaurantId);

            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);
            return;
        }

        // Create reservation
        Reservation reservation = result.getReservation();
        request.setAttribute("customerName", reservation.getCustomerName());
        request.setAttribute("reservationDate", dateStr);
        request.setAttribute("reservationTime", timeStr);
        request.setAttribute("partySize", partySize);
        request.setAttribute("confirmationId", reservation.getId());

        request.setAttribute("email", email);
        request.setAttribute("guestAllergens", allergies);
        request.setAttribute("guestComments", note);

        request.getRequestDispatcher("/WEB-INF/confirm-reservation.jsp")
                .forward(request, response);

    }
}
