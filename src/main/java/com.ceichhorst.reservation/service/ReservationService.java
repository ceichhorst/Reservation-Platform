package com.ceichhorst.reservation.service;

import com.ceichhorst.reservation.dao.ReservationDao;
import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.entity.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationService {

    private ReservationDao reservationDao = new ReservationDao();
    private ServiceInstanceDao serviceInstanceDao = new ServiceInstanceDao();

    public ReservationResult createReservation (
            Long restaurantId,
            String dateStr,
            String timeStr,
            int partySize,
            String name,
            String email,
            String allergies,
            String note
    ) {

        LocalDate date = LocalDate.parse(dateStr);
        LocalTime time = LocalTime.parse(timeStr);

        // Find service instance
        List<ServiceInstance> services =
                serviceInstanceDao.getServicesByRestaurantOnDate(restaurantId, date);

        ServiceInstance selected = services.stream()
                .filter(s -> s.getServiceTime().equals(time))
                .findFirst()
                .orElse(null);

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

        // Success
        return ReservationResult.success(reservation);

    }

}
