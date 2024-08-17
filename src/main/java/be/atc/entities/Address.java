package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "addresses")
@NamedQueries({
        @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a"),
        @NamedQuery(name = "Address.findById", query = "SELECT a FROM Address a WHERE a.id = :id"),
        @NamedQuery(name = "Address.findByStreetName", query = "SELECT a FROM Address a WHERE a.streetName = :streetName"),
        @NamedQuery(name = "Address.findByLocalityId", query = "SELECT a FROM Address a WHERE a.fkLocality.id = :localityId")
})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private int id;

    @Column(name = "street_name", nullable = false)
    private String streetName;

    @Column(name = "number")
    private String number;

    @Column(name = "box_number", length = 11)
    private String boxNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_locality_id", nullable = true)
    private Locality fkLocality;
    @OneToMany(mappedBy = "fkAddresse")
    private Set<User> users = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public Locality getFkLocality() {
        return fkLocality;
    }

    public void setFkLocality(Locality fkLocality) {
        this.fkLocality = fkLocality;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}