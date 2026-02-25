package edu.matc.entity;

import jakarta.persistence.*;

import java.util.List;

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