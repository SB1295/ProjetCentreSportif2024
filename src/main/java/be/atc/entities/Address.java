package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Représente une adresse dans le système.
 * Cette entité est mappée à la table "addresses" de la base de données.
 */
@Entity
@Table(name = "addresses")
@NamedQueries({
        @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a"),
        @NamedQuery(name = "Address.findById", query = "SELECT a FROM Address a WHERE a.id = :id"),
        @NamedQuery(name = "Address.findByStreetName", query = "SELECT a FROM Address a WHERE a.streetName = :streetName"),
        @NamedQuery(name = "Address.findByLocalityId", query = "SELECT a FROM Address a WHERE a.fkLocality.id = :localityId")
})
public class Address {

    /**
     * Identifiant unique de l'adresse.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private int id;

    /**
     * Nom de la rue associée à cette adresse.
     */
    @Column(name = "street_name", nullable = false)
    private String streetName;

    /**
     * Numéro de rue associé à cette adresse.
     */
    @Column(name = "number")
    private String number;

    /**
     * Numéro de boîte associé à cette adresse, si applicable.
     */
    @Column(name = "box_number", length = 11)
    private String boxNumber;

    /**
     * Localité à laquelle cette adresse appartient.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fk_locality_id", nullable = true)
    private Locality fkLocality;

    /**
     * Utilisateurs associés à cette adresse.
     */
    @OneToMany(mappedBy = "fkAddresse")
    private Set<User> users = new LinkedHashSet<>();

    // Getters et Setters

    /**
     * Obtient l'identifiant unique de l'adresse.
     * @return L'identifiant unique de l'adresse.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de l'adresse.
     * @param id L'identifiant unique de l'adresse.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le nom de la rue associée à cette adresse.
     * @return Le nom de la rue.
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * Définit le nom de la rue associée à cette adresse.
     * @param streetName Le nom de la rue.
     */
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    /**
     * Obtient le numéro de rue associé à cette adresse.
     * @return Le numéro de rue.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Définit le numéro de rue associé à cette adresse.
     * @param number Le numéro de rue.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Obtient le numéro de boîte associé à cette adresse, si applicable.
     * @return Le numéro de boîte.
     */
    public String getBoxNumber() {
        return boxNumber;
    }

    /**
     * Définit le numéro de boîte associé à cette adresse, si applicable.
     * @param boxNumber Le numéro de boîte.
     */
    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    /**
     * Obtient la localité à laquelle cette adresse appartient.
     * @return La localité associée.
     */
    public Locality getFkLocality() {
        return fkLocality;
    }

    /**
     * Définit la localité à laquelle cette adresse appartient.
     * @param fkLocality La localité associée.
     */
    public void setFkLocality(Locality fkLocality) {
        this.fkLocality = fkLocality;
    }

    /**
     * Obtient les utilisateurs associés à cette adresse.
     * @return Un ensemble d'utilisateurs.
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Définit les utilisateurs associés à cette adresse.
     * @param users Un ensemble d'utilisateurs.
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
