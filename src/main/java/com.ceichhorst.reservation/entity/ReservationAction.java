package com.ceichhorst.reservation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an audit log entry for an administrative action
 * performed on a {@link Reservation}.
 *
 * <p>Each {@code ReservationAction} records:</p>
 * <ul>
 *     <li>The reservation affected</li>
 *     <li>The administrator who performed the action</li>
 *     <li>The type of action taken</li>
 *     <li>The timestamp when the action occurred</li>
 * </ul>
 *
 * <p>This entity supports reservation auditing and administrative history tracking,
 * allowing the system to maintain a chronological record of changes such as:</p>
 * <ul>
 *     <li>Reservation confirmations</li>
 *     <li>Reservation cancellations</li>
 *     <li>Reservation updates or edits</li>
 * </ul>
 *
 * <p>Each action is automatically timestamped when persisted using the
 * {@link PrePersist} lifecycle callback.</p>
 *
 * <p>This entity maps to the {@code reservation_action} table in the
 * reservation platform database.</p>
 *
 * @author ceichhorst
 */
@Entity
@Table(name = "reservation_action")
public class ReservationAction {

    /**
     * Unique identifier for the reservation action record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The reservation affected by this action.
     */
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    /**
     * The administrator who performed the action.
     */
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Administrator admin;

    /**
     * The type of administrative action performed.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ReservationActionType action;

    /**
     * Timestamp indicating when the action occurred.
     */
    @Column(name = "action_time")
    private LocalDateTime actionTime;

    /**
     * Helper method to set timestamps automatically
     */
    @PrePersist
    protected void onCreate() {
        actionTime = LocalDateTime.now();
    }

    /**
     * Gets the unique identifier for this reservation action.
     * @return the reservation action ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this reservation action.
     * @param id the reservation action ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the reservation associated with this action.
     * @return the affected reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * Sets the reservation associated with this action.
     * @param reservation the reservation affected by the action
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * Gets the administrator who performed the action.
     * @return the acting administrator
     */
    public Administrator getAdmin() {
        return admin;
    }

    /**
     * Sets the administrator who performed the action.
     * @param admin the administrator responsible for the action
     */
    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    /**
     * Gets the type of action performed on the reservation.
     * @return the reservation action type
     */
    public ReservationActionType getAction() {
        return action;
    }

    /**
     * Sets the type of action performed on the reservation.
     * @param action the reservation action type to set
     */
    public void setAction(ReservationActionType action) {
        this.action = action;
    }

    /**
     * Gets the timestamp indicating when the action occurred.
     * @return the action timestamp
     */
    public LocalDateTime getActionTime() {
        return actionTime;
    }

    /**
     * Sets the timestamp indicating when the action occurred.
     * @param actionTime the action timestamp to set
     */
    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }
}
