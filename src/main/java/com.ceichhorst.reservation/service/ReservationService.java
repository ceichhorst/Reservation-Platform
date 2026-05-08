package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service layer responsible for handling reservation creation workflows.
 *
 * <p>This class coordinates between data access objects and domain entities
 * to process reservation requests, including:</p>
 * <ul>
 *   <li>Parsing and validating input data</li>
 *   <li>Locating the appropriate {@link ServiceInstance}</li>
 *   <li>Constructing a {@link Reservation} entity</li>
 *   <li>Delegating concurrency-safe persistence to {@link ReservationDao}</li>
 * </ul>
 *
 * @author ceichhorst
 */
public class ReservationService {

    private ReservationDao reservationDao = new ReservationDao();
    private ServiceInstanceDao serviceInstanceDao = new ServiceInstanceDao();
    private EmailService emailService;

    public ReservationService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Attempts to create a reservation for a given restaurant, date, and time.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Parses the provided date and time strings into {@link LocalDate} and {@link LocalTime}</li>
     *   <li>Retrieves all {@link ServiceInstance} objects for the specified restaurant and date</li>
     *   <li>Selects the service instance matching the requested time</li>
     *   <li>Constructs a {@link Reservation} entity with the provided details</li>
     *   <li>Attempts to persist the reservation using a concurrency-safe DAO method</li>
     * </ol>
     *
     * <p>If the requested time slot does not exist, or if capacity has already been
     * reached, the operation will fail with an appropriate message.</p>
     *
     * @param restaurantId the ID of the restaurant
     * @param serviceInstanceId - the service instance id associated with the requested date and time
     * @param partySize the number of guests for the reservation
     * @param name the customer's name
     * @param email the customer's email address
     * @param allergies any allergy information provided by the customer
     * @param note additional comments or special requests
     * @return a {@link ReservationResult} indicating success or failure;
     *         on success, contains the created reservation
     */
    public ReservationResult createReservation (
            Long restaurantId,
            Long serviceInstanceId,
            int partySize,
            String name,
            String email,
            String allergies,
            String note
    ) {

        ServiceInstance selected = serviceInstanceDao.getById(serviceInstanceId);

        if (selected == null) {
            return ReservationResult.failure("Invalid time selection");
        }

        // Build reservation
        Reservation reservation = new Reservation();
        reservation.setCustomerName(name);
        reservation.setEmail(email);
        reservation.setAllergenInfo(allergies);
        reservation.setAdditionalComments(note);
        reservation.setServiceInstance(selected);
        reservation.setPartySize(partySize);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        // Attempt booking
        boolean success = reservationDao.createReservationIfAvailable((reservation));

        if (!success) {
            return ReservationResult.failure("That time slot is full.");
        }

        // Send confirmation email after successful reservation confirmation
        String formattedTime = selected.getServiceTime()
                        .format(DateTimeFormatter.ofPattern("h:mm a"));

        String formattedDate = selected.getServiceDate().toString();

        emailService.sendReservationConfirmation(
                email,
                name,
                formattedDate,
                formattedTime,
                partySize,
                allergies,
                note
        );

        // Success
        return ReservationResult.success(reservation);

    }

}
