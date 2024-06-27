package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hall_id", nullable = false)
    private int id;

    @Column(name = "label_hall", nullable = false)
    private String labelHall;

    @Column(name = "active", nullable = false)
    private boolean active = false;

    @OneToMany(mappedBy = "fkHall")
    private Set<Field> fields = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabelHall() {
        return labelHall;
    }

    public void setLabelHall(String labelHall) {
        this.labelHall = labelHall;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Field> getFields() {
        return fields;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

}