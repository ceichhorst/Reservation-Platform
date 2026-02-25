package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;
import com.ceichhorst.reservation.service.ServiceInstance;
import java.util.List;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "service_instance_id", nullable = false)
    private ServiceInstance serviceInstance;

    private String customerName;
    private String email;
    private int partySize;

    @Column(columnDefinition = "TEXT")
    private String allergenInfo;

    @Column(columnDefinition = "TEXT")
    private String additionalComments;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // getters & setters

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public String getAllergenInfo() {
        return allergenInfo;
    }

    public void setAllergenInfo(String allergenInfo) {
        this.allergenInfo = allergenInfo;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}