package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;

import java.util.List;

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
    private List<ServiceInstance> serviceInstances;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<ServiceTemplate> serviceTemplates;

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
}