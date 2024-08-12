package be.atc.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NamedQueries({
        @NamedQuery(name = "Role.findById", query = "SELECT r FROM Role r WHERE r.id = :id"),
        @NamedQuery(name = "Role.findByName", query = "SELECT r FROM Role r WHERE r.roleName = :roleName"),
        @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r") // NamedQuery pour findAll

})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private int id;

    @Lob
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "fkRole")
    private Set<User> users = new LinkedHashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}