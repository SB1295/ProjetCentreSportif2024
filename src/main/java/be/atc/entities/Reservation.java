package be.atc.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private int id;

    @Column(name = "name_reservation", nullable = false)
    private String nameReservation;

    @Column(name = "price", nullable = false, precision = 10)
    private BigDecimal price;

    @Lob
    @Column(name = "statut_payement")
    private String statutPayement;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "active", nullable = false)
    private boolean active = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User fkUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_time_slot_id", nullable = false)
    private TimeSlot fkTimeSlot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_sport_field_id", nullable = false)
    private SportField fkSportField;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameReservation() {
        return nameReservation;
    }

    public void setNameReservation(String nameReservation) {
        this.nameReservation = nameReservation;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatutPayement() {
        return statutPayement;
    }

    public void setStatutPayement(String statutPayement) {
        this.statutPayement = statutPayement;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getFkUser() {
        return fkUser;
    }

    public void setFkUser(User fkUser) {
        this.fkUser = fkUser;
    }

    public TimeSlot getFkTimeSlot() {
        return fkTimeSlot;
    }

    public void setFkTimeSlot(TimeSlot fkTimeSlot) {
        this.fkTimeSlot = fkTimeSlot;
    }

    public SportField getFkSportField() {
        return fkSportField;
    }

    public void setFkSportField(SportField fkSportField) {
        this.fkSportField = fkSportField;
    }

}