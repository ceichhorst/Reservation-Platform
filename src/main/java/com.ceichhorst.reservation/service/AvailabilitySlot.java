package com.ceichhorst.reservation.service;

public class AvailabilitySlot {

    private Long id;
    private String serviceTimeFormatted;

    public AvailabilitySlot(Long id, String serviceTimeFormatted) {
        this.id = id;
        this.serviceTimeFormatted = serviceTimeFormatted;
    }

    public Long getId() {
        return id;
    }

    public String getServiceTimeFormatted() {
        return serviceTimeFormatted;
    }
}
