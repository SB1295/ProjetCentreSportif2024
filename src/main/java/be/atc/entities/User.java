package be.atc.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
// Sofiân - Gestion des utilisateurs
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false, length = 90)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 90)
    private String lastName;

    @Column(name = "birthdate", nullable = true)
    private LocalDate birthdate;

    @Lob
    @Column(name = "gender", nullable = true)
    private String gender;

    @Column(name = "phone", nullable = true, length = 20)
    private String phone;

    @Column(name = "blacklist", nullable = false)
    private boolean blacklist = false;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_role_id")
    private Role fkRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_addresse_id")
    private Address fkAddresse;

    @OneToMany(mappedBy = "fkUser")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fkUser")
    private Set<Reservation> reservations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fkUser")
    private Set<UsersSubscription> usersSubscriptions = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Role getFkRole() {
        return fkRole;
    }

    public void setFkRole(Role fkRole) {
        this.fkRole = fkRole;
    }

    public Address getFkAddresse() {
        return fkAddresse;
    }

    public void setFkAddresse(Address fkAddresse) {
        this.fkAddresse = fkAddresse;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<UsersSubscription> getUsersSubscriptions() {
        return usersSubscriptions;
    }

    public void setUsersSubscriptions(Set<UsersSubscription> usersSubscriptions) {
        this.usersSubscriptions = usersSubscriptions;
    }

}