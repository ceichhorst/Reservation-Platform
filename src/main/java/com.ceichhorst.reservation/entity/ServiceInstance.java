package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entity representing a specific scheduled occurrence of a restaurant service.
 *
 * <p>A {@code ServiceInstance} defines when a service takes place (date and time),
 * how many guests it can accommodate, and which reservations are associated with it.</p>
 *
 * <p>This is a core scheduling construct in the system, typically derived from a
 * service template but representing a concrete, bookable event.</p>
 *
 * @author ceichhorst
 */
@Entity
@Table(name = "service_instance")
public class ServiceInstance {

    /**
     * Unique identifier for the service instance.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The restaurant that owns this service instance.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    /**
     * The date on which the service occurs.
     */
    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;

    /**
     * The start time of the service.
     */
    @Column(name = "service_time", nullable = false)
    private LocalTime serviceTime;

    /**
     * The end time of the service.
     */
    @Column(name = "end_time")
    private LocalTime endTime;

    /**
     * Formatted version of {@link #serviceTime}
     */
    @Transient
    private String serviceTimeFormatted;

    /**
     * Formatted version of {@link #endTime}
     */
    @Transient
    private String endTimeFormatted;

    /**
     * Maximum number of guests that can be accommodated for this service instance.
     */
    @Column(nullable = false)
    private int capacity;

    /**
     * Version field used for optimistic locking.
     */
    @Version
    private int version;

    /**
     * Indicates whether this service instance is visible and available for booking.
     */
    @Column(name = "visible")
    private Boolean visible;

    /**
     * List of reservations associated with this service instance.
     */
    @OneToMany(mappedBy = "serviceInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Reservation> reservations;

    // getters & setters ==================================================================

    /**
     * Gets the unique identifier of the service instance.
     * @return the service instance ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the service instance
     * @param id the service instance ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the associated restaurant
     * @return the restaurant
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Sets the associated restaurant
     * @param restaurant the restaurant to set
     */
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Gets the date on which this service instance occurs.
     * @return the service date
     */
    public LocalDate getServiceDate() {
        return serviceDate;
    }

    /**
     * Sets the date on which this service instance occurs.
     * @param serviceDate the service date to set
     */
    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    /**
     * Gets the start time of the service.
     * @return the service start time
     */
    public LocalTime getServiceTime() {
        return serviceTime;
    }

    /**
     * Gets the end time of the service
     * @return the service end time
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the service
     * @param endTime the service end time to set
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Sets the start time of the service
     * @param serviceTime the service start time to set
     */
    public void setServiceTime(LocalTime serviceTime) {
        this.serviceTime = serviceTime;
    }

    /**
     * Gets a formatted representation of the service start time
     * @return the formatted service start time
     */
    public String getServiceTimeFormatted() {
        return serviceTimeFormatted;
    }

    /**
     * Sets a formatted representation of the service end time
     * @param serviceTimeFormatted the formatted service set time to set
     */
    public void setServiceTimeFormatted(String serviceTimeFormatted) {
        this.serviceTimeFormatted = serviceTimeFormatted;
    }

    /**
     * Gets a formatted representation of the service end time
     * @return the formatted service end time
     */
    public String getEndTimeFormatted() {
        return endTimeFormatted;
    }

    /**
     * Sets a formatted representation of the service end time
     * @param endTimeFormatted the formatted service end time to setg
     */
    public void setEndTimeFormatted(String endTimeFormatted) {
        this.endTimeFormatted = endTimeFormatted;
    }

    /**
     * Gets the maximum number of guests allowed for this service instance
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the maximum number of guests allowed for this service instance
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the version used for optimistic locking
     * @return the current version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the version used for optimistic locking
     * @param version the current version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Indicates whether this service instance is visible and available for booking.
     * @return {@code true} if visible; {@code false} otherwise
     */
    public Boolean getVisible() {
        return visible;
    }

    /**
     * Sets whether this service instance is visible and available for booking.
     * @param visible {@code true} if visible; {@code false} otherwise
     */
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets the list of reservations associated with this service instance.
     * @return the list of reservations (may be empty)
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Sets the list of reservations associated with this service instance.
     * @param reservations the list of reservations to associate
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}