package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Représente un rôle dans le système.
 * Cette entité est mappée à la table "roles" de la base de données.
 */
@Entity
@Table(name = "roles")
@NamedQueries({
        @NamedQuery(name = "Role.findById", query = "SELECT r FROM Role r WHERE r.id = :id"),
        @NamedQuery(name = "Role.findByName", query = "SELECT r FROM Role r WHERE r.roleName = :roleName"),
        @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r") // NamedQuery pour findAll
})
public class Role {

    /**
     * Identifiant unique du rôle.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private int id;

    /**
     * Nom du rôle.
     */
    @Lob
    @Column(name = "role_name", nullable = false)
    private String roleName;

    /**
     * Ensemble des utilisateurs associés à ce rôle.
     */
    @OneToMany(mappedBy = "fkRole")
    private Set<User> users = new LinkedHashSet<>();

    /**
     * Obtient l'identifiant unique du rôle.
     * @return L'identifiant unique du rôle.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique du rôle.
     * @param id L'identifiant unique du rôle.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient le nom du rôle.
     * @return Le nom du rôle.
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Définit le nom du rôle.
     * @param roleName Le nom du rôle.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Obtient l'ensemble des utilisateurs associés à ce rôle.
     * @return Un ensemble d'utilisateurs.
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Définit l'ensemble des utilisateurs associés à ce rôle.
     * @param users Un ensemble d'utilisateurs.
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
