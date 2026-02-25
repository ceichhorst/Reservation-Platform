package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.service.ServiceInstance;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ReservationDaoTest {

    @Test
    void testSaveReservation() {
        ReservationDao dao = new ReservationDao();

        ServiceInstance service = new ServiceInstance();
        service.setCapacity(10);

        Reservation reservation = new Reservation();
        reservation.setCustomerName("Test User");
        reservation.setPartySize(2);
        reservation.setServiceInstance(service);

        dao.save(reservation);

        assertNotNull(reservation.getId());
    }
}