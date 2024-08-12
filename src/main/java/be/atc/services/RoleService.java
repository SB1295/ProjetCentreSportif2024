package be.atc.services;

import be.atc.entities.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    // Récupérer un rôle par Id
    Optional<Role> findRoleById(int id);
    // Récupérer tous les rôles
    List<Role> findAllRoles();
    // Créer un rôle
    void createRole(Role role);
    // Supprimer un rôle
    void deleteRoleById(int id);
    // Mettre à jour un rôle existant
    void updateRole(Role role);
}

