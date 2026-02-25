package edu.matc.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "restaurant")
public class Restaurant (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private boolean requireAllergenInfo;

    @Enumerated(EnumType.STRING)
    private SchedulingType schedulingType;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<ServiceInstance> serviceInstances;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<ServiceTemplate> serviceTemplates;

    // getters & setters (toString?)
)