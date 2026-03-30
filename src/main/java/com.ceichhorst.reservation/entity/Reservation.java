package com.ceichhorst.reservation.entity;

import javax.persistence.*;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_instance_id", nullable = false)
    @JsonBackReference
    private ServiceInstance serviceInstance;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "party_size", nullable = false)
    private int partySize;

    @Column(columnDefinition = "TEXT")
    private String allergenInfo;

    @Column(columnDefinition = "TEXT")
    private String additionalComments;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    // getters & setters

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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