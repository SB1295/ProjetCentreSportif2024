package be.atc.dao;

import be.atc.entities.Role;

import java.util.Optional;
import java.util.List;

/**
 * Interface pour gérer les opérations de base de données liées aux entités {@link Role}.
 */
public interface RoleDao {

    /**
     * Trouve un rôle par son ID.
     *
     * @param id L'ID du rôle à rechercher.
     * @return Un {@link Optional} contenant le rôle s'il est trouvé, ou vide sinon.
     */
    Optional<Role> findRoleById(int id);

    /**
     * Trouve tous les rôles disponibles dans la base de données.
     *
     * @return Une liste de tous les rôles.
     */
    List<Role> findAllRoles();

    /**
     * Crée un nouveau rôle dans la base de données.
     *
     * @param role Le rôle à créer.
     */
    void createRole(Role role);

    /**
     * Supprime un rôle de la base de données en fonction de son ID.
     *
     * @param id L'ID du rôle à supprimer.
     */
    void deleteRoleById(int id);

    /**
     * Met à jour un rôle existant dans la base de données.
     *
     * @param role Le rôle avec les informations mises à jour.
     */
    void updateRole(Role role);
}
