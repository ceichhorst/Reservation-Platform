package com.ceichhorst.reservation.entity;

import com.ceichhorst.reservation.service.ServiceTemplate;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a restaurant within the reservation system.
 *
 * <p>This class maps to the {@code restaurant} table in the {@code reservation_platform} database.</p>
 *
 * <p>A restaurant serves as the top-level domain object that defines how reservations are structured and managed.
 * It owns service instances, which are specific scheduled occurrences.</p>
 *
 * <p>This entity also captures configuration settings such as scheduling behavior, requiring allergen information,
 * and descriptive content used for display.</p>
 *
 * <p>The {@code id} is responsible for providing the correct restaurant being utilized within this
 * platform, as the application is individualized and set to only works with one restaurant in the application at a
 * time.</p>
 *
 * @author ceichhorst
 *
 */
@Entity
@Table(name = "restaurant")
public class Restaurant {

    /**
     * Unique identifier for a restaurant.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Display name of the restaurant.
     */
    private String name;

    /**
     * Email of the restaurant
     */
    private String email;

    /**
     * Indicates whether restaurants require customers to provide allergen/dietary information
     * when making a reservation.
     */
    @Column(name = "require_allergen_info")
    private boolean requireAllergenInfo;

    /**
     * Defines how the restaurant schedules its services.
     */
    @Column(name = "scheduling_type")
    @Enumerated(EnumType.STRING)
    private SchedulingType schedulingType;

    /**
     * List of service instances associated with this restaurant.
     */
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ServiceInstance> serviceInstances;

    // Consider removing service templates
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<ServiceTemplate> serviceTemplates;

    /**
     * Set of administrators who handle this restaurant.
     */
    @ManyToMany(mappedBy = "restaurants")
    private Set<Administrator> administrators = new HashSet<>();

    /**
     * City where the restaurant is located.
     */
    @Column(name = "city")
    private String city;

    /**
     * State where the restaurant is located.
     */
    @Column(name = "state")
    private String state;

    /**
     * Description of the restaurant.
     */
    @Column(name = "description")
    private String description;

    /**
     * Additional explanatory content describing how the reservation or dining experience
     * works for their establishment.
     */
    @Column(name = "how_it_works")
    private String howItWorks;

    // getters & setters ================================================================

    /**
     * Gets the unique ID of the reservation.
     * @return the restaurant ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the restaurant.
     * @param id the restaurant ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the restaurant.
     * @return the restaurant name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the restaurant.
     * @param name the restaurant name.
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Indicates whether allergen info is required for reservations.
     * @return {@code true} if allergen info is required; {@code false} otherwise.
     */
    public boolean isRequireAllergenInfo() {
        return requireAllergenInfo;
    }

    /**
     * Sets whether allergen info is required for reservations.
     * @param requireAllergenInfo {@code true} if allergen info is required; {@code false} otherwise.
     */
    public void setRequireAllergenInfo(boolean requireAllergenInfo) {
        this.requireAllergenInfo = requireAllergenInfo;
    }

    /**
     * Gets the scheduling type used by the restaurant.
     * @return the scheduling type.
     */
    public SchedulingType getSchedulingType() {
        return schedulingType;
    }

    /**
     * Sets the scheduling type used by the restaurant.
     * @param schedulingType the scheduling type to set.
     */
    public void setSchedulingType(SchedulingType schedulingType) {
        this.schedulingType = schedulingType;
    }

    /**
     * Gets the list of service instances.
     * @return list of service instances.
     */
    public List<ServiceInstance> getServiceInstances() {
        return serviceInstances;
    }

    /**
     * Sets the list of service instances.
     * @param serviceInstances list of service instances to set.
     */
    public void setServiceInstances(List<ServiceInstance> serviceInstances) {
        this.serviceInstances = serviceInstances;
    }

    // Consider removing
    public List<ServiceTemplate> getServiceTemplates() {
        return serviceTemplates;
    }

    // Consider removing
    public void setServiceTemplates(List<ServiceTemplate> serviceTemplates) {
        this.serviceTemplates = serviceTemplates;
    }

    /**
     * Gets the administrators managing the restaurant
     * @return set of administrators
     */
    public Set<Administrator> getAdministrators() {
        return administrators;
    }

    /**
     * Sets the administrators managing the restaurant.
     * @param administrators the administrators to associate
     */
    public void setAdministrators(Set<Administrator> administrators) {
        this.administrators = administrators;
    }

    /**
     * Gets the restaurant's city.
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the restaurant's city.
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the restaurant's state.
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the restaurant's state.
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the restaurant's description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the restaurant's description
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the 'how it works' information content.
     * @return explanatory content for users
     */
    public String getHowItWorks() {
        return howItWorks;
    }

    /**
     * Sets the 'how it works' information content
     * @param howItWorks explanatory content to set for users
     */
    public void setHowItWorks(String howItWorks) {
        this.howItWorks = howItWorks;
    }
}