package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service_template")
public class ServiceTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int dayOfWeek;

    private java.time.LocalTime serviceTime;

    private int capacity;

    @ManyToOne
    @JoinColumn(name = "restaruant_id")
    private Restaurant restaurant;

    // getters & setters
}