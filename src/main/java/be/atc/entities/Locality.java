package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "localities")
@NamedQueries({
        @NamedQuery(name = "Locality.findAll", query = "SELECT l FROM Locality l"),
        @NamedQuery(name = "Locality.findById", query = "SELECT l FROM Locality l WHERE l.id = :id"),
        @NamedQuery(name = "Locality.findByPostalCode", query = "SELECT l FROM Locality l WHERE l.postalCode = :postalCode"),
        @NamedQuery(name = "Locality.findByTown", query = "SELECT l FROM Locality l WHERE l.town = :town"),
        @NamedQuery(name = "Locality.findByProvince", query = "SELECT l FROM Locality l WHERE l.province = :province"),
        @NamedQuery(name = "Locality.findByCountry", query = "SELECT l FROM Locality l WHERE l.fkCountry.id = :countryId")
})
public class Locality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locality_id", nullable = false)
    private int id;

    @Column(name = "postal_code", length = 4)
    private String postalCode;

    @Column(name = "town", length = 50)
    private String town;

    @Column(name = "sub_town", length = 3)
    private String subTown;

    @Column(name = "main_town", length = 50)
    private String maintown;

    @Column(name = "province", length = 19)
    private String province;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_country_id", nullable = false)
    private Country fkCountry;

    @OneToMany(mappedBy = "fkLocality")
    private Set<Address> addresses = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getSubTown() {
        return subTown;
    }

    public void setSubTown(String subTown) {
        this.subTown = subTown;
    }

    public String getMaintown() {
        return maintown;
    }

    public void setMaintown(String maintown) {
        this.maintown = maintown;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Country getFkCountry() {
        return fkCountry;
    }

    public void setFkCountry(Country fkCountry) {
        this.fkCountry = fkCountry;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

}