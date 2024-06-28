package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sport_field")
public class SportField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sport_field_id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_field_id", nullable = false)
    private Field fkField;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_sport_id", nullable = false)
    private Sport fkSport;

    @OneToMany(mappedBy = "fkSportField")
    private Set<Close> closes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fkSportField")
    private Set<Reservation> reservations = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Field getFkField() {
        return fkField;
    }

    public void setFkField(Field fkField) {
        this.fkField = fkField;
    }

    public Sport getFkSport() {
        return fkSport;
    }

    public void setFkSport(Sport fkSport) {
        this.fkSport = fkSport;
    }

    public Set<Close> getCloses() {
        return closes;
    }

    public void setCloses(Set<Close> closes) {
        this.closes = closes;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

}