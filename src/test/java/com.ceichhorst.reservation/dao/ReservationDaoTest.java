package com.ceichhorst.reservation.dao;

import java.io.InputStream;
import java.util.Properties;

import com.ceichhorst.reservation.entity.Reservation;
import com.ceichhorst.reservation.service.ServiceInstance;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReservationDaoTest {

    private static Properties properties = new Properties();

    @BeforeAll
    static void setup() {
        try (InputStream input = ReservationDaoTest.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find database.properties");
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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