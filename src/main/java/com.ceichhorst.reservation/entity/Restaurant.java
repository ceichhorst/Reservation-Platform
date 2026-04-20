package com.ceichhorst.reservation.entity;

import com.ceichhorst.reservation.service.ServiceTemplate;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private boolean requireAllergenInfo;

    @Enumerated(EnumType.STRING)
    private SchedulingType schedulingType;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ServiceInstance> serviceInstances;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<ServiceTemplate> serviceTemplates;

    @ManyToMany(mappedBy = "restaurants")
    private Set<Administrator> administrators = new HashSet<>();

    private String city;
    private String state;
    private String description;

    // getters & setters (toString?)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequireAllergenInfo() {
        return requireAllergenInfo;
    }

    public void setRequireAllergenInfo(boolean requireAllergenInfo) {
        this.requireAllergenInfo = requireAllergenInfo;
    }

    public SchedulingType getSchedulingType() {
        return schedulingType;
    }

    public void setSchedulingType(SchedulingType schedulingType) {
        this.schedulingType = schedulingType;
    }

    public List<ServiceInstance> getServiceInstances() {
        return serviceInstances;
    }

    public void setServiceInstances(List<ServiceInstance> serviceInstances) {
        this.serviceInstances = serviceInstances;
    }

    public List<ServiceTemplate> getServiceTemplates() {
        return serviceTemplates;
    }

    public void setServiceTemplates(List<ServiceTemplate> serviceTemplates) {
        this.serviceTemplates = serviceTemplates;
    }

    public Set<Administrator> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(Set<Administrator> administrators) {
        this.administrators = administrators;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}