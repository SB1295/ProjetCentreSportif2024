package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sports")
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sport_id", nullable = false)
    private int id;

    @Column(name = "sport_label", nullable = false)
    private String sportLabel;

    @Column(name = "active", nullable = false)
    private boolean active = false;

    @OneToMany(mappedBy = "fkSport")
    private Set<SportField> sportFields = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fkSport")
    private Set<Subscription> subscriptions = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSportLabel() {
        return sportLabel;
    }

    public void setSportLabel(String sportLabel) {
        this.sportLabel = sportLabel;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<SportField> getSportFields() {
        return sportFields;
    }

    public void setSportFields(Set<SportField> sportFields) {
        this.sportFields = sportFields;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

}