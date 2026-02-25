package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "service_instance")
public class ServiceInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private java.time.LocalDate serviceDate;

    private java.time.LocalTime serviceTime;

    private int capacity;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "restaruant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "serviceInstance", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    // getters & setters
}