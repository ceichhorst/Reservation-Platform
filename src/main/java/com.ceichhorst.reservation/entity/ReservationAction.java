package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing n action taken on a reservation by an admin
 *
 * @author ceichhorst
 */
@Entity
@Table(name = "reservation_action")
public class ReservationAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Administrator admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ReservationActionType action;

    @Column(name = "action_time")
    private LocalDateTime actionTime;

    /**
     * Helper method to set timestamps automatically
     */
    @PrePersist
    protected void onCreate() {
        actionTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Administrator getAdmin() {
        return admin;
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    public ReservationAction getAction() {
        return action;
    }

    public void setAction(ReservationAction action) {
        this.action = action;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }
}
