package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing an administrative user within the reservation system.
 *
 * <p>Administrators are responsible for managing one or more {@link Restaurant}
 * entities, including configuration, scheduling, and operational oversight.</p>
 *
 * <p>This entity captures authentication-related identity (username, email),
 * authorization role, and audit metadata such as account creation and last login time.</p>
 *
 * @author ceichhorst
 */
@Entity
@Table(name = "administrator")
public class Administrator {

    /**
     * Unique identifier for the administrator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username used for authentication/identification.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Unique email used for authentication/identification.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Role assigned to the administrator.
     */
    @Column(name = "role", nullable = false)
    private String role;

    /**
     * Timestamp indicating when the administrator account was created for auditing purposes
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the administrator's most recent login.
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // Many to many relationship
    /**
     * Set of restaurants managed by this administrator.
     */
    @ManyToMany
    @JoinTable(
            name = "admin_restaurant",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant> restaurants = new HashSet<>();

    // getters & setters ================================================

    /**
     * Gets the unique identifier of the administrator.
     * @return the administrator id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the administrator.
     * @param id the administrator id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the role of the admin.
     * @return the role of the admin
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the admin.
     * @param role the role of the admin
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the account creation timestamp.
     * @return the creation timestamp.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the account creation timestamp.
     * @param createdAt the timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the timestamp of the last login.
     * @return the last login timestamp
     */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets the timestamp of the last login.
     * @param lastLogin the timestamp to set
     */
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Gets the set of restaurants managed by this administrator.
     * @return set of associated restaurants
     */
    public Set<Restaurant> getRestaurants() {
        return restaurants;
    }

    /**
     * Sets the restaurants managed by this administrator.
     * @param restaurants the restaurants to associate
     */
    public void setRestaurants(Set<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }


}
