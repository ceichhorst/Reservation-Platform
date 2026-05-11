package com.ceichhorst.reservation.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTimeFormatterTest {

    private ServiceTimeFormatter formatter;

    @BeforeEach
    void setup() {
        formatter = new ServiceTimeFormatter();
    }

    @Test
    void testFormatTimes_standardTime() {
        ServiceInstance service = new ServiceInstance();
        service.setServiceTime(LocalTime.of(18, 30));
        service.setEndTime(LocalTime.of(20, 0));

        List<ServiceInstance> services = List.of(service);
        formatter.formatTimes(services);

        assertEquals("6:30 PM", service.getServiceTimeFormatted());
        assertEquals("8:00 PM", service.getEndTimeFormatted());
    }

    @Test
    void testFormatTimes_midnight() {
        ServiceInstance service = new ServiceInstance();
        service.setServiceTime(LocalTime.MIDNIGHT);

        List<ServiceInstance> services = List.of(service);
        formatter.formatTimes(services);

        assertEquals("12:00 AM", service.getServiceTimeFormatted());
    }

    @Test
    void testFormatTimes_noon() {
        ServiceInstance service = new ServiceInstance();
        service.setServiceTime(LocalTime.NOON);

        List<ServiceInstance> services = List.of(service);
        formatter.formatTimes(services);

        assertEquals("12:00 PM", service.getServiceTimeFormatted());
    }

    @Test
    void testFormatTimes_nullServiceTime() {
        ServiceInstance service = new ServiceInstance();
        service.setServiceTime(null);
        service.setEndTime(null);

        List<ServiceInstance> services = List.of(service);
        formatter.formatTimes(services);

        assertNull(service.getServiceTimeFormatted());
        assertNull(service.getEndTimeFormatted())
    }

    @Test
    void testFormatTimes_multipleServices() {
        ServiceInstance s1 = new ServiceInstance();
        s1.setServiceTime(LocalTime.of(16, 0));

        ServiceInstance s2 = new ServiceInstance();
        s2.setServiceTime(LocalTime.of(17, 15));

        ServiceInstance s3 = new ServiceInstance();
        s3.setServiceTime(LocalTime.of(19, 45));


        List<ServiceInstance> services = new ArrayList<>();
        services.add(s1);
        services.add(s2);
        services.add(s3);
        formatter.formatTimes(services);

        assertEquals("4:00 PM", s1.getServiceTimeFormatted());
        assertEquals("5:15 PM", s2.getServiceTimeFormatted());
        assertEquals("7:45 PM", s3.getServiceTimeFormatted());
    }

    @Test
    void testFormatTimes_returnOriginalList() {
        ServiceInstance service = new ServiceInstance();
        service.setServiceTime(LocalTime.of(18, 0));

        List<ServiceInstance> original = new ArrayList<>();
        original.add(service);

        List<ServiceInstance> returned = formatter.formatTimes(original);

        assertSame(original, returned);
    }

    @Test
    void testFormatTimes_emptyList() {
        List<ServiceInstance> services = new ArrayList<>();
        List<ServiceInstance> result = formatter.formatTimes(services);
        assertTrue(result.isEmpty());
    }
}
