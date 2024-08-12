package be.atc.dao;

import be.atc.entities.Role;

import java.util.Optional;
import java.util.List;

public interface RoleDao {

    // Méthode pour trouver un rôle par son ID
    Optional<Role> findRoleById(int id);

    // Méthode pour trouver tous les rôles
    List<Role> findAllRoles();

    // Méthode pour créer un nouveau rôle
    void createRole(Role role);

    // Méthode pour supprimer un rôle par son ID
    void deleteRoleById(int id);

    // Méthode pour mettre à jour un rôle
    void updateRole(Role role);
}
