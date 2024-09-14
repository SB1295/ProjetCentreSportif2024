package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Représente une localité dans le système.
 * Cette entité est mappée à la table "localities" de la base de données.
 */
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

    /**
     * Identifiant unique de la localité.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locality_id", nullable = false)
    private int id;

    /**
     * Code postal de la localité.
     */
    @Column(name = "postal_code", length = 4)
    private String postalCode;

    /**
     * Ville de la localité.
     */
    @Column(name = "town", length = 50)
    private String town;

    /**
     * Sous-ville de la localité (le cas échéant).
     */
    @Column(name = "sub_town", length = 3)
    private String subTown;

    /**
     * Ville principale de la localité.
     */
    @Column(name = "main_town", length = 50)
    private String maintown;

    /**
     * Province à laquelle appartient la localité.
     */
    @Column(name = "province", length = 19)
    private String province;

    /**
     * Pays auquel appartient la localité.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_country_id", nullable = false)
    private Country fkCountry;

    /**
     * Ensemble des adresses associées à cette localité.
     */
    @OneToMany(mappedBy = "fkLocality")
    private Set<Address> addresses = new LinkedHashSet<>();

    // Getters et Setters

    /**
     * Obtient l'identifiant unique de la localité.
     * @return L'identifiant unique de la localité.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de la localité.
     * @param id L'identifiant unique de la localité.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le code postal de la localité.
     * @return Le code postal de la localité.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Définit le code postal de la localité.
     * @param postalCode Le code postal de la localité.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Obtient la ville de la localité.
     * @return La ville de la localité.
     */
    public String getTown() {
        return town;
    }

    /**
     * Définit la ville de la localité.
     * @param town La ville de la localité.
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * Obtient la sous-ville de la localité (le cas échéant).
     * @return La sous-ville de la localité.
     */
    public String getSubTown() {
        return subTown;
    }

    /**
     * Définit la sous-ville de la localité (le cas échéant).
     * @param subTown La sous-ville de la localité.
     */
    public void setSubTown(String subTown) {
        this.subTown = subTown;
    }

    /**
     * Obtient la ville principale de la localité.
     * @return La ville principale de la localité.
     */
    public String getMaintown() {
        return maintown;
    }

    /**
     * Définit la ville principale de la localité.
     * @param maintown La ville principale de la localité.
     */
    public void setMaintown(String maintown) {
        this.maintown = maintown;
    }

    /**
     * Obtient la province à laquelle appartient la localité.
     * @return La province de la localité.
     */
    public String getProvince() {
        return province;
    }

    /**
     * Définit la province à laquelle appartient la localité.
     * @param province La province de la localité.
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Obtient le pays auquel appartient la localité.
     * @return Le pays de la localité.
     */
    public Country getFkCountry() {
        return fkCountry;
    }

    /**
     * Définit le pays auquel appartient la localité.
     * @param fkCountry Le pays de la localité.
     */
    public void setFkCountry(Country fkCountry) {
        this.fkCountry = fkCountry;
    }

    /**
     * Obtient les adresses associées à cette localité.
     * @return Un ensemble d'adresses.
     */
    public Set<Address> getAddresses() {
        return addresses;
    }

    /**
     * Définit les adresses associées à cette localité.
     * @param addresses Un ensemble d'adresses.
     */
    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }
}
