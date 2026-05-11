package com.ceichhorst.reservation.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

public class CalendarTimeSlotTest {

    @Test
    void testGetHour_returnsCorrectHour() {
        CalendarTimeSlot slot = new CalendarTimeSlot();
        slot.setServiceTime(LocalTime.of(18, 30));
        assertEquals(18, slot.getHour());
    }

    @Test
    void testGetHour_nullServiceTime_returnsNegativeOne() {
        CalendarTimeSlot slot = new CalendarTimeSlot();
        slot.setServiceTime(null);
        assertEquals(-1, slot.getHour());
    }

    @Test
    void testIsFull_whenRemainingSeatsIsZero() {
        CalendarTimeSlot slot = new CalendarTimeSlot();
        slot.setRemainingSeats(0);
        slot.setFull(true);
        assertTrue(slot.isFull());
    }

    @Test
    void testIsNotNull_whenSeatsRemain() {
        CalendarTimeSlot slot = new CalendarTimeSlot();
        slot.setRemainingSeats(5);
        slot.setFull(false);
        assertFalse(slot.isFull());
    }

    @Test
    void testServiceTimeFormatted_setAndGet() {
        CalendarTimeSlot slot = new CalendarTimeSlot();
        slot.setServiceTimeFormatted("6:00 PM");
        assertEquals("6:00 PM", slot.getServiceTimeFormatted());
    }
}
