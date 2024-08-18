package be.atc.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Représente un utilisateur dans le système.
 * Cette entité est mappée à la table "users" de la base de données.
 */
@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query = "select u from User u where u.email = :email"),
        @NamedQuery(name = "User.findAll", query = "select u from User u"),
        @NamedQuery(name = "User.existsByEmail", query = "select count(u) from User u where u.email = :email"),
        @NamedQuery(name = "User.deleteByEmail", query = "delete from User u where u.email = :email"),
        @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id")
})
public class User {

    /**
     * Identifiant unique de l'utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int id;

    /**
     * Adresse e-mail de l'utilisateur.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Prénom de l'utilisateur.
     */
    @Column(name = "first_name", nullable = false, length = 90)
    private String firstName;

    /**
     * Nom de famille de l'utilisateur.
     */
    @Column(name = "last_name", nullable = false, length = 90)
    private String lastName;

    /**
     * Date de naissance de l'utilisateur.
     */
    @Column(name = "birthdate", nullable = true)
    private LocalDate birthdate;

    /**
     * Genre de l'utilisateur.
     */
    @Lob
    @Column(name = "gender", nullable = true)
    private String gender;

    /**
     * Numéro de téléphone de l'utilisateur.
     */
    @Column(name = "phone", nullable = true, length = 20)
    private String phone;

    /**
     * Indique si l'utilisateur est blacklisté.
     */
    @Column(name = "blacklist", nullable = false)
    private boolean blacklist = false;

    /**
     * Indique si l'utilisateur est actif.
     */
    @Column(name = "active", nullable = false)
    private boolean active = true;

    /**
     * Rôle associé à l'utilisateur.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_role_id")
    private Role fkRole;

    /**
     * Adresse associée à l'utilisateur.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_addresse_id")
    private Address fkAddresse;

    /**
     * Ensemble des commandes passées par l'utilisateur.
     */
    @OneToMany(mappedBy = "fkUser")
    private Set<Order> orders = new LinkedHashSet<>();

    /**
     * Ensemble des réservations effectuées par l'utilisateur.
     */
    @OneToMany(mappedBy = "fkUser")
    private Set<Reservation> reservations = new LinkedHashSet<>();

    /**
     * Ensemble des abonnements de l'utilisateur.
     */
    @OneToMany(mappedBy = "fkUser")
    private Set<UsersSubscription> usersSubscriptions = new LinkedHashSet<>();

    // Getters et setters

    /**
     * Obtient l'identifiant unique de l'utilisateur.
     * @return L'identifiant unique de l'utilisateur.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de l'utilisateur.
     * @param id L'identifiant unique de l'utilisateur.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient l'adresse e-mail de l'utilisateur.
     * @return L'adresse e-mail de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse e-mail de l'utilisateur.
     * @param email L'adresse e-mail de l'utilisateur.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtient le mot de passe de l'utilisateur.
     * @return Le mot de passe de l'utilisateur.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtient le prénom de l'utilisateur.
     * @return Le prénom de l'utilisateur.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Définit le prénom de l'utilisateur.
     * @param firstName Le prénom de l'utilisateur.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Obtient le nom de famille de l'utilisateur.
     * @return Le nom de famille de l'utilisateur.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Définit le nom de famille de l'utilisateur.
     * @param lastName Le nom de famille de l'utilisateur.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Obtient la date de naissance de l'utilisateur.
     * @return La date de naissance de l'utilisateur.
     */
    public LocalDate getBirthdate() {
        return birthdate;
    }

    /**
     * Définit la date de naissance de l'utilisateur.
     * @param birthdate La date de naissance de l'utilisateur.
     */
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Obtient le genre de l'utilisateur.
     * @return Le genre de l'utilisateur.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Définit le genre de l'utilisateur.
     * @param gender Le genre de l'utilisateur.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Obtient le numéro de téléphone de l'utilisateur.
     * @return Le numéro de téléphone de l'utilisateur.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Définit le numéro de téléphone de l'utilisateur.
     * @param phone Le numéro de téléphone de l'utilisateur.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Vérifie si l'utilisateur est blacklisté.
     * @return True si l'utilisateur est blacklisté, sinon false.
     */
    public boolean getBlacklist() {
        return blacklist;
    }

    /**
     * Définit si l'utilisateur est blacklisté.
     * @param blacklist True si l'utilisateur est blacklisté, sinon false.
     */
    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    /**
     * Vérifie si l'utilisateur est actif.
     * @return True si l'utilisateur est actif, sinon false.
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Définit si l'utilisateur est actif.
     * @param active True si l'utilisateur est actif, sinon false.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Obtient le rôle associé à l'utilisateur.
     * @return Le rôle associé à l'utilisateur.
     */
    public Role getFkRole() {
        return fkRole;
    }

    /**
     * Définit le rôle associé à l'utilisateur.
     * @param fkRole Le rôle associé à l'utilisateur.
     */
    public void setFkRole(Role fkRole) {
        this.fkRole = fkRole;
    }

    /**
     * Obtient l'adresse associée à l'utilisateur.
     * @return L'adresse associée à l'utilisateur.
     */
    public Address getFkAddresse() {
        return fkAddresse;
    }

    /**
     * Définit l'adresse associée à l'utilisateur.
     * @param fkAddresse L'adresse associée à l'utilisateur.
     */
    public void setFkAddresse(Address fkAddresse) {
        this.fkAddresse = fkAddresse;
    }

    /**
     * Obtient l'ensemble des commandes passées par l'utilisateur.
     * @return Un ensemble de commandes.
     */
    public Set<Order> getOrders() {
        return orders;
    }

    /**
     * Définit l'ensemble des commandes passées par l'utilisateur.
     * @param orders Un ensemble de commandes.
     */
    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    /**
     * Obtient l'ensemble des réservations effectuées par l'utilisateur.
     * @return Un ensemble de réservations.
     */
    public Set<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Définit l'ensemble des réservations effectuées par l'utilisateur.
     * @param reservations Un ensemble de réservations.
     */
    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * Obtient l'ensemble des abonnements de l'utilisateur.
     * @return Un ensemble d'abonnements.
     */
    public Set<UsersSubscription> getUsersSubscriptions() {
        return usersSubscriptions;
    }

    /**
     * Définit l'ensemble des abonnements de l'utilisateur.
     * @param usersSubscriptions Un ensemble d'abonnements.
     */
    public void setUsersSubscriptions(Set<UsersSubscription> usersSubscriptions) {
        this.usersSubscriptions = usersSubscriptions;
    }

}
