package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String customerName;
    private String email;
    private int partySize;

    @Column(columnDefinition = "TEXT")
    private String allergenInfo;

    @Column(columnDefinition = "TEXT")
    private String additionalComments;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "service_instance_id")
    private ServiceInstance serviceInstance;

    // getters & setters
}