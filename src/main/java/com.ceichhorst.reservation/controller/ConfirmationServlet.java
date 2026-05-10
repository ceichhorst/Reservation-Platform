package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ReservationService;
import com.ceichhorst.reservation.service.ReservationResult;
import com.ceichhorst.reservation.service.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Servlet responsible for confirming and finalizing customer reservations.
 *
 * <p>This servlet represents the final step in the reservation workflow. It processes
 * form data submitted by the user, validates inputs, and delegates reservation
 * creation to {@link ReservationService}.</p>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Validate user-submitted reservation details</li>
 *   <li>Ensure session state is valid (restaurant context)</li>
 *   <li>Delegate reservation creation to the service layer</li>
 *   <li>Handle success and failure outcomes</li>
 *   <li>Forward to confirmation or return to input form as needed</li>
 * </ul>
 *
 * <p><strong>Flow:</strong></p>
 * <ol>
 *   <li>User submits reservation details form</li>
 *   <li>Servlet validates required fields and input formats</li>
 *   <li>{@link ReservationService} attempts to create the reservation</li>
 *   <li>On success → forward to confirmation page</li>
 *   <li>On failure → return to form with error message</li>
 * </ol>
 *
 * <p>This servlet relies on {@link ReservationResult} to distinguish between
 * successful and failed reservation attempts without using exceptions for
 * expected business outcomes (e.g., full capacity).</p>
 *
 * @author ceichhorst
 */
@WebServlet("/confirm-reservation")
public class ConfirmationServlet extends HttpServlet{

    /**
     * Service responsible for reservation creation and business logic.
     */
    private ReservationService reservationService = new ReservationService(new EmailService());
    private ServiceInstanceDao serviceInstanceDao = new ServiceInstanceDao();

    /**
     * Handles HTTP POST requests to finalize a reservation.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates session state to ensure a restaurant context exists</li>
     *   <li>Extracts and validates request parameters</li>
     *   <li>Converts input values to appropriate types</li>
     *   <li>Delegates reservation creation to {@link ReservationService}</li>
     *   <li>Handles success or failure outcomes</li>
     *   <li>Forwards to the appropriate JSP view</li>
     * </ol>
     *
     * <p><strong>Required request parameters:</strong></p>
     * <ul>
     *   <li>{@code customerName}</li>
     *   <li>{@code email}</li>
     *   <li>{@code reservationDate} (ISO-8601 format, {@code yyyy-MM-dd})</li>
     *   <li>{@code reservationTime} (ISO-8601 format, {@code HH:mm})</li>
     *   <li>{@code partySize}</li>
     *   <li>{@code restaurantId}</li>
     * </ul>
     *
     * <p><strong>Optional parameters:</strong></p>
     * <ul>
     *   <li>{@code guestAllergies}</li>
     *   <li>{@code guestNotes}</li>
     * </ul>
     *
     * <p><strong>Success behavior:</strong></p>
     * <ul>
     *   <li>Sets confirmation details as request attributes</li>
     *   <li>Forwards to {@code /WEB-INF/confirm-reservation.jsp}</li>
     * </ul>
     *
     * <p><strong>Failure behavior:</strong></p>
     * <ul>
     *   <li>Sets error message and preserves user input</li>
     *   <li>Forwards back to {@code /WEB-INF/reservation-details.jsp}</li>
     * </ul>
     *
     * @param request the HTTP request containing form data
     * @param response the HTTP response
     * @throws ServletException if processing fails
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Restaurant restaurant = (Restaurant) session.getAttribute("restaurant");

        if (restaurant == null) {
            request.setAttribute("message", "Session expired. Please restart reservation.");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Extract form data
        String name = request.getParameter("customerName");
        String email = request.getParameter("email");
        String dateStr = request.getParameter("reservationDate");
        String serviceInstanceIdStr = request.getParameter("serviceInstanceId");
        String partySizeStr = request.getParameter("partySize");
        String restaurantIdStr = request.getParameter("restaurantId");
        String allergies = request.getParameter("guestAllergies");
        String note = request.getParameter("guestNotes");

        // Allergen check if required by restaurant
        if (restaurant.isRequireAllergenInfo()
                && (allergies == null || allergies.trim().isEmpty())) {

            request.setAttribute(
                    "message",
                    "Allergy/Dietary information is required."
            );

            request.setAttribute("requireAllergenInfo", restaurant.isRequireAllergenInfo());
            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);

            return;
        }

        // Validation
        if (name == null || email == null || dateStr == null || serviceInstanceIdStr == null ||
            partySizeStr == null || restaurantIdStr == null || name.isEmpty() ||
            email.isEmpty() || dateStr.isEmpty() || serviceInstanceIdStr.isEmpty() || partySizeStr.isEmpty() ||
            restaurantIdStr.isEmpty()) {

            request.setAttribute("message", "All required fields must be filled out.");
            request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                    .forward(request, response);
            return;
        }

        try {
            Long restaurantId = Long.parseLong(restaurantIdStr);
            Long serviceInstanceId = Long.parseLong(serviceInstanceIdStr);
            int partySize = Integer.parseInt(partySizeStr);

            ServiceInstance instance = serviceInstanceDao.getById(serviceInstanceId);

            if (instance == null) {
                request.setAttribute("message", "Invalid service selection.");
                request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                        .forward(request, response);
                return;
            }

            // Find matching service instance
            ReservationResult result = reservationService.createReservation(
                    restaurantId,
                    serviceInstanceId,
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
                request.setAttribute("serviceInstanceId", serviceInstanceId);
                request.setAttribute("reservationDate", instance.getServiceDate().toString());
                request.setAttribute("reservationTime", instance.getServiceTimeFormatted());
                request.setAttribute("partySize", partySize);
                request.setAttribute("restaurantId", restaurantId);

                request.getRequestDispatcher("/WEB-INF/reservation-details.jsp")
                        .forward(request, response);
                return;
            }

            String formattedTime = instance.getServiceTime()
                    .format(DateTimeFormatter.ofPattern("h:mm a"));

            // Create reservation
            Reservation reservation = result.getReservation();
            request.setAttribute("restaurantName", restaurant.getName());
            request.setAttribute("restaurantEmail", restaurant.getEmail());
            request.setAttribute("customerName", reservation.getCustomerName());
            request.setAttribute("serviceInstanceId", serviceInstanceId);
            request.setAttribute("reservationDate", instance.getServiceDate().toString());
            request.setAttribute("reservationTime", formattedTime);
            request.setAttribute("partySize", partySize);
            request.setAttribute("confirmationId", reservation.getId());

            request.setAttribute("email", email);
            request.setAttribute("guestAllergens", allergies);
            request.setAttribute("guestComments", note);

            request.setAttribute("restaurant", restaurant);

            request.getRequestDispatcher("/WEB-INF/confirm-reservation.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error processing reservation", e);
        }
    }
}
