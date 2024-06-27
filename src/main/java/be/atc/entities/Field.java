package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "fields")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id", nullable = false)
    private int id;

    @Column(name = "label_field", nullable = false)
    private String labelField;

    @Column(name = "active", nullable = false)
    private boolean active = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_hall_id", nullable = false)
    private Hall fkHall;

    @OneToMany(mappedBy = "fkField")
    private Set<SportField> sportFields = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Hall getFkHall() {
        return fkHall;
    }

    public void setFkHall(Hall fkHall) {
        this.fkHall = fkHall;
    }

    public Set<SportField> getSportFields() {
        return sportFields;
    }

    public void setSportFields(Set<SportField> sportFields) {
        this.sportFields = sportFields;
    }

}