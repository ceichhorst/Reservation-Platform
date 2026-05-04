package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;
import com.ceichhorst.reservation.service.ServiceInstance;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;

/**
 * Entity representing a reservation made by a customer for a specific {@link ServiceInstance}.
 *
 * <p>This class maps to the {@code reservation} table in the {@code reservation_platform} database
 * and captures all relevant details about a reservation, including customer information,
 * party size, dietary restrictions, and reservation status.</p>
 *
 * <p>Each reservation is associated with exactly one service instance,
 * but a service instance may have multiple reservations.</p>
 *
 * @author ceichhorst
 */
@Entity
@Table(name = "reservation")
public class Reservation {

    /**
     * Unique identifier for the reservation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The service instance this reservation is associated with.
     */
    @ManyToOne
    @JoinColumn(name = "service_instance_id", nullable = false)
    @JsonBackReference
    private ServiceInstance serviceInstance;

    /**
     * Name of the customer who made the reservation.
     */
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    /**
     * Email address of the customer.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * Number of people included in the reservation.
     */
    @Column(name = "party_size", nullable = false)
    private int partySize;

    /**
     * Information about allergens or dietary restrictions provided by the customer.
     */
    @Column(columnDefinition = "TEXT")
    private String allergenInfo;

    /**
     * Additional comments or special requests from the customer.
     */
    @Column(columnDefinition = "TEXT")
    private String additionalComments;

    /**
     * Current status of the reservation.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    // getters & setters =========================================================

    /**
     * Gets the associated service instance.
     * @return the service instance linked to this reservation.
     */
    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    /**
     * Sets the associated service instance.
     * @param serviceInstance the service instance to associate with this reservation.
     */
    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    /**
     * Gets the unique ID of the reservation.
     * @return the reservation ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the reservation.
     * @param id the reservation ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the current reservation status.
     * @return the reservation status.
     */
    public ReservationStatus getStatus() {
        return status;
    }

    /**
     * Sets the current reservation status ID.
     * @param status the reservation status to set.
     */
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    /**
     * Gets additional comments or special requests.
     * @return additional comments provided by the customer.
     */
    public String getAdditionalComments() {
        return additionalComments;
    }

    /**
     * Sets additional comments or special requests.
     * @param additionalComments the comments to set.
     */
    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    /**
     * Gets allergen or dietary restriction info.
     * @return allergen information.
     */
    public String getAllergenInfo() {
        return allergenInfo;
    }

    /**
     * Sets allergen or dietary restriction info.
     * @param allergenInfo the allergen info to set.
     */
    public void setAllergenInfo(String allergenInfo) {
        this.allergenInfo = allergenInfo;
    }

    /**
     * Gets the party size for the reservation.
     * @return number of people in the reservation.
     */
    public int getPartySize() {
        return partySize;
    }

    /**
     * Sets the party size for the reservation.
     * @param partySize the number of people to set.
     */
    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    /**
     * Gets the customer's email.
     * @return the email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the customer's email.
     * @param email the email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the customer's name.
     * @return the email address.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer's name.
     * @param customerName the name of the customer to set.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}