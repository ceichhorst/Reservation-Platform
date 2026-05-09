package com.ceichhorst.reservation.service;

public class AvailabilitySlot {

    private Long id;
    private String serviceTimeFormatted;
    private int remainingSeats;

    public AvailabilitySlot(Long id, String serviceTimeFormatted, int remainingSeats) {
        this.id = id;
        this.serviceTimeFormatted = serviceTimeFormatted;
        this.remainingSeats = remainingSeats;
    }

    public Long getId() {
        return id;
    }

    public String getServiceTimeFormatted() {
        return serviceTimeFormatted;
    }

    public int getRemainingSeats() {return remainingSeats;};
}
